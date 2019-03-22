package adapter.web.controllers

import java.util.concurrent.TimeUnit

import application.cache.CacheRepository
import application.inputport.{LinkSocialAccountUseCaseInputPort, UserSignInUseCaseInputPort}
import domain.social.SocialClientId
import javax.inject.{Inject, Singleton}
import play.api.Configuration
import play.api.mvc.{AnyContent, ControllerComponents, Request}
import twitter4j.auth.RequestToken
import twitter4j.{Twitter, TwitterFactory}

import scala.concurrent.duration.Duration

@Singleton
class TwitterOAuthController @Inject()(cc: ControllerComponents, config: Configuration, cacheRepository: CacheRepository, userSignInUseCaseInputPort: UserSignInUseCaseInputPort, linkSocialAccountUseCaseInputPort: LinkSocialAccountUseCaseInputPort) extends OAuthController(cacheRepository, cc) {
  override def signIn() = Action { implicit request: Request[AnyContent] =>
    request.session.get("accessToken").map{sessionKey =>
      val twitter = new TwitterFactory().getInstance()
      val requestToken: RequestToken = twitter.getOAuthRequestToken("http://127.0.0.1:9000/signin_callback/twitter")
      cacheRepository.setCache(sessionKey, "twitter", twitter, Duration.apply(120, TimeUnit.SECONDS))
      cacheRepository.setCache(sessionKey, "requestToken", requestToken, Duration.apply(120, TimeUnit.SECONDS))
      Redirect(requestToken.getAuthorizationURL)
    }.getOrElse(Redirect(adapter.web.controllers.routes.HomeController.index()))
  }

  override def signInCallback() = Action { implicit request: Request[AnyContent] =>
    request.queryString.get("denied") match {
      case Some(_) => Redirect(adapter.web.controllers.routes.HomeController.index())
      case _ => {
        val tokenOption = request.session.get("accessToken")
        val twitterOption = tokenOption.flatMap(sessionKey => cacheRepository.getCache[Twitter](sessionKey, "twitter"))
        val requestTokenOption = tokenOption.flatMap(sessionKey => cacheRepository.getCache[RequestToken](sessionKey, "requestToken"))
        val authVerifierOption = request.getQueryString("oauth_verifier")
        tokenOption.foreach{sessionKey =>
          cacheRepository.remove(sessionKey, "twitter")
          cacheRepository.remove(sessionKey, "requestToken")
        }

        val accessTokenOption = for {
          twitter <- twitterOption
          requestToken <- requestTokenOption
          authVerifier <- authVerifierOption
        } yield twitter.getOAuthAccessToken(requestToken, authVerifier)

        accessTokenOption.map{accessToken =>
          tokenOption match {
            case Some(token) => {
              linkSocialAccountUseCaseInputPort.link(token, SocialClientId.Twitter, accessToken.getToken, accessToken.getTokenSecret) match {
                case Right(_) => {
                  println("連携成功")
                  Redirect(adapter.web.controllers.routes.SummaryController.index())
                }
                case Left(message) => Redirect(adapter.web.controllers.routes.HomeController.index()).flashing(("message", message))
              }
            }
            case _ => {
              userSignInUseCaseInputPort.signInWith(SocialClientId.Twitter, accessToken.getToken(), accessToken.getTokenSecret())
              match {
                case Right(result) => {
                  Redirect(adapter.web.controllers.routes.SummaryController.index()).withSession(("accessToken", result.value))
                }
                case _ => {
                  Redirect(adapter.web.controllers.routes.HomeController.index())
                }
              }
            }
          }
        }.getOrElse(Redirect(adapter.web.controllers.routes.HomeController.index()))
      }
    }
  }
}
