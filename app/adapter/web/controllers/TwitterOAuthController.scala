package adapter.web.controllers

import java.util.concurrent.TimeUnit

import application.cache.{CacheRepository, SignUpCache}
import application.coordinator.UserCoordinator
import application.inputport.{LinkSocialAccountUseCaseInputPort, UserSignInUseCaseInputPort}
import controllers.routes
import domain.model.social.SocialClientId
import javax.inject.{Inject, Singleton}
import play.api.Configuration
import play.api.mvc.{AnyContent, ControllerComponents, Request}
import twitter4j.auth.RequestToken
import twitter4j.{Twitter, TwitterFactory}

import scala.concurrent.duration.Duration

@Singleton
class TwitterOAuthController @Inject()(cc: ControllerComponents, userCoordinator: UserCoordinator, config: Configuration, cacheRepository: CacheRepository, userSignInUseCaseInputPort: UserSignInUseCaseInputPort, linkSocialAccountUseCaseInputPort: LinkSocialAccountUseCaseInputPort) extends OAuthController(cacheRepository, cc) {
  override def signIn() = Action { implicit request: Request[AnyContent] =>
    request.session.get(config.get[String]("session.cookieName")).map{sessionKey =>
      val twitter = new TwitterFactory().getInstance()
      val requestToken: RequestToken = twitter.getOAuthRequestToken("http://127.0.0.1:9000/signin_callback/twitter")
      cacheRepository.setCache(sessionKey, "twitter", twitter, Duration.apply(120, TimeUnit.SECONDS))
      cacheRepository.setCache(sessionKey, "requestToken", requestToken, Duration.apply(120, TimeUnit.SECONDS))
      Redirect(requestToken.getAuthorizationURL)
    }.getOrElse(Redirect(routes.HomeController.index()))
  }

  override def signInCallback() = Action { implicit request: Request[AnyContent] =>
    request.queryString.get("denied") match {
      case Some(_) => Redirect(routes.HomeController.index())
      case _ => {
        val sessionKeyOption = request.session.get(config.get[String]("session.cookieName"))
        val twitterOption = sessionKeyOption.flatMap(sessionKey => cacheRepository.getCache[Twitter](sessionKey, "twitter"))
        val requestTokenOption = sessionKeyOption.flatMap(sessionKey => cacheRepository.getCache[RequestToken](sessionKey, "requestToken"))
        val authVerifierOption = request.getQueryString("oauth_verifier")
        sessionKeyOption.foreach{sessionKey =>
          cacheRepository.remove(sessionKey, "twitter")
          cacheRepository.remove(sessionKey, "requestToken")
        }

        val accessTokenOption = for {
          twitter <- twitterOption
          requestToken <- requestTokenOption
          authVerifier <- authVerifierOption
        } yield twitter.getOAuthAccessToken(requestToken, authVerifier)

        val sessionKey = sessionKeyOption.get
        accessTokenOption.map{accessToken =>
          cacheRepository.getCache[SignUpCache](sessionKey, config.get[String]("app.signup.cache_name")) match {
            case Some(signInCache) => {
              linkSocialAccountUseCaseInputPort.link(sessionKey, SocialClientId.Twitter, accessToken.getToken, accessToken.getTokenSecret) match {
                case Right(socialAccount) => {
                  println("連携成功")
                  cacheRepository.setCache(sessionKey, config.get[String]("app.signup.cache_name"), signInCache.cacheSocialAccount(socialAccount))
                  Redirect(routes.SummaryController.index())
                }
                case Left(message) => Redirect(routes.HomeController.index()).flashing(("message", message))
              }
            }
            case _ => {
              userSignInUseCaseInputPort.signInWith(SocialClientId.Twitter, accessToken.getToken(), accessToken.getTokenSecret())
              match {
                case Right(result) => {
                  Redirect(routes.SummaryController.index()).withSession(("accessToken", result.value))
                }
                case _ => {
                  Redirect(routes.HomeController.index())
                }
              }
            }
          }
        }.getOrElse(Redirect(routes.HomeController.index()))
      }
    }
  }
}
