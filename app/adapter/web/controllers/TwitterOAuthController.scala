package adapter.web.controllers

import java.util.concurrent.TimeUnit

import application.cache.CacheRepository
import application.inputport.{CheckRegistrationStatusUseCaseInputPort, LinkSocialAccountUseCaseInputPort, UserSignInUseCaseInputPort}
import domain.social.SocialClientId
import domain.user.RegistrationStatus.{Regular, Temporary}
import javax.inject.{Inject, Singleton}
import play.api.Configuration
import play.api.mvc.{AnyContent, ControllerComponents, Request}
import twitter4j.auth.RequestToken
import twitter4j.{Twitter, TwitterFactory}

import scala.concurrent.duration.Duration

@Singleton
class TwitterOAuthController @Inject()(cc: ControllerComponents, cacheRepository: CacheRepository, userSignInUseCaseInputPort: UserSignInUseCaseInputPort, linkSocialAccountUseCaseInputPort: LinkSocialAccountUseCaseInputPort, checkRegistrationStatusUseCaseInputPort: CheckRegistrationStatusUseCaseInputPort, implicit config: Configuration) extends OAuthController(cacheRepository, cc) {
  override def signIn() = Action { implicit request: Request[AnyContent] =>
    request.session.get("accessToken").map{sessionKey =>
      val twitterOAuth = new TwitterOAuth()
      val requestToken: RequestToken = twitterOAuth.getRequestToken
      cacheRepository.setCache(sessionKey, "twitter", twitterOAuth, Duration.apply(120, TimeUnit.SECONDS))
      cacheRepository.setCache(sessionKey, "requestToken", requestToken, Duration.apply(120, TimeUnit.SECONDS))
      Redirect(requestToken.getAuthorizationURL)
    }.getOrElse(Redirect(adapter.web.controllers.routes.HomeController.index()))
  }

  override def signInCallback() = Action { implicit request: Request[AnyContent] =>
    request.queryString.get("denied") match {
      case Some(_) => Redirect(adapter.web.controllers.routes.HomeController.index())
      case _ => {
        val tokenOption = request.session.get("accessToken")
        val twitterOption = tokenOption.flatMap(sessionKey => cacheRepository.getCache[TwitterOAuth](sessionKey, "twitter"))
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
                  checkRegistrationStatusUseCaseInputPort.registrationStatus(token) match {
                    case Temporary => Redirect(adapter.web.controllers.routes.SignUpController.complete())
                    case Regular => Redirect(adapter.web.controllers.routes.SummaryController.index())
                  }
                }
                case Left(message) => Redirect(adapter.web.controllers.routes.HomeController.index()).flashing(("message", message))
              }
            }
            case _ => {
              userSignInUseCaseInputPort.signInWith(SocialClientId.Twitter, accessToken.getToken, accessToken.getTokenSecret)
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

class TwitterOAuth(implicit configuration: Configuration) {

  private val twitter = new TwitterFactory().getInstance()
  private val config = OAuthConfig.configLoader.load(configuration.underlying, SocialClientId.Twitter.value)
  private val requestToken = twitter.getOAuthRequestToken(config.callbackUrl)

  def getRequestToken = requestToken

  def getAuthorizationURL = requestToken.getAuthorizationURL

  def getOAuthAccessToken(requestToken: RequestToken, oauthVerifier: String) = twitter.getOAuthAccessToken(requestToken, oauthVerifier)
}
