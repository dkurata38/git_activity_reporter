package controllers

import java.util.concurrent.TimeUnit

import application.cache.{SignInCache, SignUpCache}
import application.coordinator.UserCoordinator
import domain.model.social.{SocialAccessToken, SocialAccount, SocialAccountId, SocialClientId}
import domain.model.user.RegistrationStatus.Regular
import javax.inject.{Inject, Singleton}
import play.api.Configuration
import play.api.cache.SyncCacheApi
import play.api.mvc.{AnyContent, ControllerComponents, Request}
import twitter4j.auth.RequestToken
import twitter4j.{Twitter, TwitterFactory}

import scala.concurrent.duration.Duration

@Singleton
class TwitterOAuthController @Inject()(cache: SyncCacheApi, cc: ControllerComponents, userCoordinator: UserCoordinator, config: Configuration) extends OAuthController(cache, cc) {
  override def signIn() = Action { implicit request: Request[AnyContent] =>
    val twitter = new TwitterFactory().getInstance()
    val requestToken: RequestToken = twitter.getOAuthRequestToken("http://127.0.0.1:9000/signin_callback/twitter")

    cache.set("twitter", twitter, Duration.apply(120, TimeUnit.SECONDS))
    cache.set("requestToken", requestToken, Duration.apply(120, TimeUnit.SECONDS))

    Redirect(requestToken.getAuthorizationURL)
  }

  override def signInCallback() = Action { implicit request: Request[AnyContent] =>
    request.queryString.get("denied") match {
      case Some(_) => Redirect(routes.HomeController.index())
      case _ => {
        val twitterOption: Option[Twitter] = cache.get("twitter")
        val requestTokenOption: Option[RequestToken] = cache.get("requestToken")

        for {
          twitter <- twitterOption
          requestToken <- requestTokenOption
        } yield {
          val authVerifier: String = request.queryString("oauth_verifier").head
          val accessToken = twitter.getOAuthAccessToken(requestToken, authVerifier)
          twitter.verifyCredentials()
          cache.remove("twitter")
          cache.remove("requestToken")

          val signInCacheOption: Option[SignInCache] = cache.get(config.get[String]("app.signin.cache_name"))
          signInCacheOption match {
            case Some(signInCache) => {
              userCoordinator.signUp(
                signInCache,
                SocialClientId.Twitter,
                SocialAccountId(accessToken.getScreenName),
                SocialAccessToken(accessToken.getToken, accessToken.getTokenSecret)
              ).foreach{newCache =>
                cache.set(config.get[String]("app.signin.cache_name"), newCache)
                signInCache.user.registrationStatus match {
                  case Regular => Redirect(routes.SummaryController.index())
                  case _ => Redirect(routes.SignUpController.complete())
                }
              }
              Redirect(routes.HomeController.index())
            }
            case _ => {
              val signInResult = userCoordinator.signIn(
                SocialClientId.Twitter,
                SocialAccountId(accessToken.getScreenName),
                SocialAccessToken(accessToken.getToken, accessToken.getTokenSecret)
              )
              signInResult match {
                case Some(result) => {
                  cache.set("signInCache", result)
                  Redirect(routes.SummaryController.index())
                }
                case _ => {
                  Redirect(routes.HomeController.index())
                }
              }
            }
          }

        }
        Redirect(routes.HomeController.index())
      }
    }
  }

  def signUp() = Action { implicit request: Request[AnyContent] =>
    val twitter = new TwitterFactory().getInstance()
    val requestToken: RequestToken = twitter.getOAuthRequestToken("http://127.0.0.1:9000/signup_callback/twitter")

    cache.set("twitter", twitter, Duration.apply(120, TimeUnit.SECONDS))
    cache.set("requestToken", requestToken, Duration.apply(120, TimeUnit.SECONDS))

    Redirect(requestToken.getAuthorizationURL)
  }

  def signUpCallback() = Action { implicit request: Request[AnyContent] =>
    request.queryString.get("denied") match {
      case Some(_) => Redirect("/")
      case _ => {
        val twitterOption: Option[Twitter] = cache.get("twitter")
        val requestTokenOption: Option[RequestToken] = cache.get("requestToken")
        for {
          twitter <- twitterOption
          requestToken <- requestTokenOption
        } yield {
          val authVerifier: String = request.queryString("oauth_verifier").head
          val accessToken = twitter.getOAuthAccessToken(requestToken, authVerifier)
          twitter.verifyCredentials()
          cache.remove("twitter")
          cache.remove("requestToken")

          val signUpCacheOption: Option[SignUpCache] = cache.get(config.get[String]("app.signup.cache_name"))
          signUpCacheOption.foreach {
            e =>
              e.socialAccount = Some(
                new SocialAccount(
                  e.userId,
                  SocialClientId.Twitter,
                  SocialAccountId(accessToken.getScreenName),
                  SocialAccessToken(accessToken.getToken, accessToken.getTokenSecret)
                )
              )
              Redirect(routes.SignUpController.complete)
          }
        }
        Redirect(routes.HomeController.index())
      }
    }
  }
}
