package controllers

import java.util.UUID
import java.util.concurrent.TimeUnit

import application.cache.SignInCache
import application.coordinator.UserCoordinator
import application.service.GitAccountService
import domain.model.git.account.AccessToken
import domain.model.git.account.GitClientId.GitHub
import domain.model.user.RegistrationStatus.Regular
import javax.inject.{Inject, Singleton}
import play.api.{Configuration, Logger}
import play.api.cache.SyncCacheApi
import play.api.libs.ws.WSClient
import play.api.mvc.{Action, AnyContent, ControllerComponents, Request}

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

@Singleton
class GithubOAuthController @Inject()(config: Configuration, ws: WSClient, cache: SyncCacheApi, cc: ControllerComponents, gitAccountService: GitAccountService, userCoordinator: UserCoordinator) extends OAuthController(cache, cc) {

  val logger = Logger(classOf[GithubOAuthController])

  override def signIn(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    val url = "https://github.com/login/oauth/authorize"
    val clientId = config.get[String]("app.github.client_id")
    val clientSecret = config.get[String]("app.github.client_secret")
    val callbackUrl = "http://127.0.0.1:9000/signin_callback/github "
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

    val accessTokenOption = combinedOption match {
      case Some(tuple) if tuple._2 == tuple._3 => {
        println("OAuthCallback成功. AccessToken取得開始.")
        val url = "https://github.com/login/oauth/access_token"
        val clientId = config.get[String]("app.github.client_id")
        val clientSecret = config.get[String]("app.github.client_secret")
        val futureAccessToken = ws.url(url)
          .addHttpHeaders(("Accept", "application/json"))
          .addQueryStringParameters(("client_id", clientId), ("client_secret", clientSecret), ("code", tuple._1))
          .withRequestTimeout(Duration.apply(10000, TimeUnit.MILLISECONDS))
          .get().map(response => (response.json \ "access_token").as[String])
        println("AccessToken取得処理完了.")
        val accessToken = Await.result(futureAccessToken, Duration.Inf)
        Some(accessToken)
      }
      case _ => {
        println("state不正/OAuthCallback不正")
        None
      }
    }

    // SignInのCache
    val signInCacheOption: Option[SignInCache] = cache.get(config.get[String]("app.signin.cache_name"))
    accessTokenOption.map{accessToken =>
      println("accessToken取得成功")
      signInCacheOption.map{signInCache =>
        println("signInCache有")
        val gitUser = gitAccountService.getAuthenticatedUser(GitHub, AccessToken(accessToken))
        println("user情報取得成功")
        userCoordinator.signUp(signInCache, GitHub, gitUser.gitUserName, AccessToken(accessToken)) match {
          case Right(newCache) => {
            cache.set(config.get[String]("app.signin.cache_name"), newCache)
            println("サインイン")
            newCache.user.registrationStatus match{
              case Regular => Redirect(routes.SummaryController.index())
              case _ => Redirect(routes.SignUpController.linkSNS())
            }
          }
          case Left(message) => {
            println("サインイン失敗")
            Redirect(routes.HomeController.index()).flashing(("message", message))
          }
        }
      }.getOrElse(Redirect(routes.HomeController.index()))
    }.getOrElse(Redirect(routes.HomeController.index()))
  }
}

