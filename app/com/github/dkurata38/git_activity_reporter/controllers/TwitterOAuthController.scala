package com.github.dkurata38.git_activity_reporter.controllers

import java.util.concurrent.TimeUnit

import com.github.dkurata38.git_activity_reporter.application.coordinator.UserCoordinator
import com.github.dkurata38.git_activity_reporter.domain.`type`.SocialClientId
import com.github.dkurata38.git_activity_reporter.domain.model.social.{SocialAccessToken, SocialAccount, SocialAccountId}
import javax.inject.{Inject, Singleton}
import play.api.cache.SyncCacheApi
import play.api.mvc.{AnyContent, ControllerComponents, Request}
import twitter4j.auth.RequestToken
import twitter4j.{Twitter, TwitterFactory}

import scala.concurrent.duration.Duration

@Singleton
class TwitterOAuthController @Inject()(cache: SyncCacheApi, cc: ControllerComponents, userCoordinator: UserCoordinator) extends OAuthController(cache, cc) {
  override def signIn() = Action { implicit request: Request[AnyContent] =>
    val twitter = new TwitterFactory().getInstance()
    val requestToken: RequestToken = twitter.getOAuthRequestToken("http://127.0.0.1:9000/twitter/signInCallback")

    cache.set("twitter", twitter, Duration.apply(120, TimeUnit.SECONDS))
    cache.set("requestToken", requestToken, Duration.apply(120, TimeUnit.SECONDS))

    Redirect(requestToken.getAuthorizationURL)
  }

  override def oauthCallback() = Action { implicit request: Request[AnyContent] =>
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
          val signInResult = userCoordinator.signIn(
            SocialClientId.Twitter,
            SocialAccountId(accessToken.getScreenName),
            SocialAccessToken(accessToken.getToken, accessToken.getTokenSecret)
          )
          cache.remove("twitter")
          cache.remove("requestToken")

          signInResult match {
            case Some(result) => cache.set("signInCache", result)
            case _ => {
              val signUpCacheOption: Option[SignUpCache] = cache.get("signUpCache")
              signUpCacheOption.foreach{
                e => e.socialAccount = Some(
                  new SocialAccount(
                    e.user.userId,
                    SocialClientId.Twitter,
                    SocialAccountId(accessToken.getScreenName),
                    SocialAccessToken(accessToken.getToken, accessToken.getTokenSecret)
                  )
                )
              }
            }
          }
        }
      }
        Redirect("/")
    }
  }
}
