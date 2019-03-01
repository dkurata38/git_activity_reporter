package controllers

import java.util.UUID
import java.util.concurrent.TimeUnit

import application.cache.SignUpCache
import application.service.GitAccountService
import domain.model.git.account.{AccessToken, GitAccount}
import domain.model.git.account.GitClientId.GitHub
import javax.inject.{Inject, Singleton}
import play.api.Configuration
import play.api.cache.SyncCacheApi
import play.api.libs.ws.WSClient
import play.api.mvc.{Action, AnyContent, ControllerComponents, Request}

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

@Singleton
class GithubOAuthController @Inject()(config: Configuration, ws: WSClient, cache: SyncCacheApi, cc: ControllerComponents, gitAccountService: GitAccountService) extends OAuthController(cache, cc) {
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

  override def signInCallback(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
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
          val gitUserOption = gitAccountService.getAuthenticatedUser(GitHub, AccessToken(accessToken))
          for {
            gitUser <- gitUserOption
          } yield {
            // SignIn and redirect to summary top
            Redirect(routes.SummaryController.index())
          }
        }
      )

      Await.result(futureAccessToken, Duration.Inf)
    }
    Redirect("/")
  }

  override def signUp(): Action[AnyContent] = Action { implicit  request: Request[AnyContent] =>
    val url = "https://github.com/login/oauth/authorize"
    val clientId = config.get[String]("app.github.client_id")
    val clientSecret = config.get[String]("app.github.client_secret")
    val callbackUrl = "http://127.0.0.1:9000/github/oauthCallback"
    val scope = "repo"
    val state = UUID.randomUUID().toString

    val redirectUrl = s"${url}?client_id=${clientId}&client_secret=${clientSecret}&redirect_url=${callbackUrl}&scope=${scope}&state=${state}"
    cache.set("state", state, Duration.apply(120, TimeUnit.SECONDS))
    Redirect(redirectUrl)

    ???
  }

  override def signUpCallback(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
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
          val signUpCacheOption: Option[SignUpCache] = cache.get(config.get[String]("app.signup.cache_name"))
          val gitUserOption = gitAccountService.getAuthenticatedUser(GitHub, AccessToken(accessToken))
          for {
            signUpCache <- signUpCacheOption
            gitUser <- gitUserOption
          } yield {
            signUpCache.gitAccount = Some(
              new GitAccount(
                signUpCache.userId,
                GitHub,
                gitUser.gitUserName,
                AccessToken(accessToken)
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

