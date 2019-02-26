package controllers

import java.util.UUID
import java.util.concurrent.TimeUnit

import domain.`type`.GitClientId.GitHub
import domain.model.git.GitAccount
import javax.inject.{Inject, Singleton}
import play.api.Configuration
import play.api.cache.SyncCacheApi
import play.api.libs.ws.WSClient
import play.api.mvc.{Action, AnyContent, ControllerComponents, Request}

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

@Singleton
class GithubOAuthController @Inject()(config: Configuration, ws: WSClient, cache: SyncCacheApi, cc: ControllerComponents) extends OAuthController(cache, cc) {
  override def signIn(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    val url = "https://github.com/login/oauth/authorize"
    val clientId = config.get[String]("app.github.client_id")
    val clientSecret = config.get[String]("app.github.client_secret")
    val callbackUrl = "http://127.0.0.1:9000/github/oauthCallback"
    val scope = "repo"
    val state = UUID.randomUUID().toString

    val redirectUrl = s"${url}?client_id=${clientId}&client_secret=${clientSecret}&redirect_url=${callbackUrl}&scope=${scope}&state=${state}"
    cache.set("state", state, Duration.apply(120, TimeUnit.SECONDS))
    Redirect(redirectUrl)
  }

  override def oauthCallback(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    val codeOption = request.getQueryString("code")
    val stateOption = request.getQueryString("state")
    val stateCachedOption: Option[String] = cache.get("state")
    for {
      code <- codeOption
      state <- stateOption
      stateCached <- stateCachedOption
    } yield {
      if (state != stateCached) {
        Redirect("/")
      }

      val url = "https://github.com/login/oauth/access_token"
      val clientId = config.get[String]("app.github.client_id")
      val clientSecret = config.get[String]("app.github.client_secret")
      val futureAccessToken = ws.url(url)
        .addHttpHeaders(("Accept", "application/json"))
        .addQueryStringParameters(("client_id", clientId), ("client_secret", clientSecret), ("code", code))
        .withRequestTimeout(Duration.apply(10000, TimeUnit.MILLISECONDS))
        .get().map(response => (response.json \ "access_token").as[String])

      futureAccessToken.onComplete(accessTokenTry =>
        accessTokenTry.foreach { accessToken =>
          val signUpCacheOption: Option[SignUpCache] = cache.get("signUpCache")
          signUpCacheOption.foreach {
            e =>
              e.gitAccount = Some(
                new GitAccount(
                  e.user.userId,
                  GitHub,
                  "",
                  accessToken
                )
              )
              Redirect(routes.SignUpController.linkSNS())
          }
        }
      )

      Await.result(futureAccessToken, Duration.Inf)
    }
    Redirect("/")
  }
}

