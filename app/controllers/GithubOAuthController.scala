package controllers

import java.util.UUID
import java.util.concurrent.TimeUnit

import application.cache.{SignInCache, SignUpCache}
import application.coordinator.UserCoordinator
import application.service.GitAccountService
import domain.model.git.account.{AccessToken, GitAccount}
import domain.model.git.account.GitClientId.GitHub
import domain.model.user.RegistrationStatus.Regular
import javax.inject.{Inject, Singleton}
import play.api.Configuration
import play.api.cache.SyncCacheApi
import play.api.libs.ws.WSClient
import play.api.mvc.{Action, AnyContent, ControllerComponents, Request}

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

@Singleton
class GithubOAuthController @Inject()(config: Configuration, ws: WSClient, cache: SyncCacheApi, cc: ControllerComponents, gitAccountService: GitAccountService, userCoordinator: UserCoordinator) extends OAuthController(cache, cc) {
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
    val codeOption: Option[String] = request.getQueryString("code")
    val stateOption = request.getQueryString("state")
    val stateCachedOption: Option[String] = cache.get("state")
    val combinedOption = for {
      code <- codeOption
      state <- stateOption
      stateCached <- stateCachedOption
    } yield (code, state, stateCached)

    val accessTokenOption = combinedOption.map{t => if (t._2 == t._3) {
        val url = "https://github.com/login/oauth/access_token"
        val clientId = config.get[String]("app.github.client_id")
        val clientSecret = config.get[String]("app.github.client_secret")
        val futureAccessToken = ws.url(url)
          .addHttpHeaders(("Accept", "application/json"))
          .addQueryStringParameters(("client_id", clientId), ("client_secret", clientSecret), ("code", t._1))
          .withRequestTimeout(Duration.apply(10000, TimeUnit.MILLISECONDS))
          .get().map(response => (response.json \ "access_token").as[String])

        Await.result(futureAccessToken, Duration.Inf)
      } else None[String]
    }


    // SignInのCache
    val signInCacheOption: Option[SignInCache] = cache.get(config.get[String]("app.signin.cache_name"))
    accessTokenOption.map{accessToken =>
      signInCacheOption.map{signInCache =>
        val gitUser = gitAccountService.getAuthenticatedUser(GitHub, AccessToken(accessToken))
        userCoordinator.signUp(signInCache, GitHub, gitUser.gitUserName, AccessToken(accessToken)).foreach{newCache =>
          cache.set(config.get[String]("app.signin.cache_name"), newCache)
          signInCache.user.registrationStatus match{
            case Regular => Redirect(routes.SummaryController.index())
            case _ => Redirect(routes.SignUpController.linkSNS())
          }
        }
        Redirect(routes.HomeController.index())
      }.getOrElse(Redirect(routes.HomeController.index()))
    }.getOrElse(Redirect(routes.HomeController.index()))
  }
}

