package adapter.web.controllers

import java.util.UUID
import java.util.concurrent.TimeUnit

import application.cache.{CacheRepository, SignUpCache}
import application.coordinator.UserCoordinator
import application.inputport.LinkGitAccountUseCaseInputPort
import controllers.routes
import domain.model.git.account.GitClientId.GitHub
import javax.inject.{Inject, Singleton}
import play.api.libs.ws.WSClient
import play.api.mvc.{Action, AnyContent, ControllerComponents, Request}
import play.api.{Configuration, Logger}

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

@Singleton
class GithubOAuthController @Inject()(config: Configuration, ws: WSClient, cacheRepository: CacheRepository, cc: ControllerComponents, userCoordinator: UserCoordinator, gitAccountOauthUseCaseInputPort: LinkGitAccountUseCaseInputPort) extends OAuthController(cacheRepository, cc) {

  val logger = Logger(classOf[GithubOAuthController])

  override def signIn(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    request.session.get(config.get[String]("session.cookieName")).map { sessionKey =>
      val gitHubOauth = new GitHubOauth(ws, config)
      cacheRepository.setCache(sessionKey, "gitHubOauth", gitHubOauth, Duration.apply(120, TimeUnit.SECONDS))
      Redirect(gitHubOauth.getAuthorizationURL)
    }.getOrElse(Redirect(routes.HomeController.index()))
  }

  override def signInCallback(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    val codeOption: Option[String] = request.getQueryString("code")
    val sessionKeyOption = request.session.get(config.get[String]("session.cookieName"))
    val stateOption = request.getQueryString("state")
    val gitHubOauthOption = sessionKeyOption.flatMap(sessionKey => cacheRepository.getCache[GitHubOauth](sessionKey, "gitHubOauth"))
    val accessTokenOption = (for {
      code <- codeOption
      state <- stateOption
      gitHubOauth <- gitHubOauthOption
    } yield gitHubOauth.getOAuthAccessToken(state, code)).flatten

    val combinedOption = for {
      sessionKey <- sessionKeyOption
      accessToken <- accessTokenOption
    } yield (sessionKey, accessToken)

    combinedOption.flatMap { tuple =>
      val sessionKey = tuple._1
      val accessToken = tuple._2
      println("accessToken取得成功")
      cacheRepository.getCache[SignUpCache](sessionKey, config.get[String]("app.signup.cache_name")).map { signUpCache =>
        println("signUpCache有")
        gitAccountOauthUseCaseInputPort.link(sessionKey, GitHub, accessToken) match {
          case Right(gitAccount) => {
            cacheRepository.setCache(sessionKey, config.get[String]("app.signup.cache_name"), signUpCache.cacheGitAccount(gitAccount))
            println("サインイン")
            Redirect(routes.SummaryController.index())
          }
          case Left(message) => {
            println("サインイン失敗")
            Redirect(routes.HomeController.index()).flashing(("message", message))
          }
        }
      }
    }.getOrElse(Redirect(routes.HomeController.index()))
  }

}

class GitHubOauth(ws: WSClient, config: Configuration) {
  private val authorizationUrl = "https://github.com/login/oauth/authorize"
  private val accessTokenUrl = "https://github.com/login/oauth/access_token"
  private val clientId = config.get[String]("app.github.client_id")
  private val clientSecret = config.get[String]("app.github.client_secret")
  private val callbackUrl = "http://127.0.0.1:9000/signin_callback/github"
  private val scope = "repo"
  private val requestToken = UUID.randomUUID().toString

  def getRequestToken = requestToken

  def getAuthorizationURL =
    s"$authorizationUrl?client_id=$clientId&client_secret=$clientSecret&redirect_url=$callbackUrl&scope=$scope&state=$requestToken"

  def getOAuthAccessToken(state: String, code: String) = {
    if (state == requestToken) {
      val futureAccessToken = ws.url(accessTokenUrl)
        .addHttpHeaders(("Accept", "application/json"))
        .addQueryStringParameters(("client_id", clientId), ("client_secret", clientSecret), ("code", code))
        .withRequestTimeout(Duration.apply(10000, TimeUnit.MILLISECONDS))
        .get().map(response => (response.json \ "access_token").as[String])
      println("AccessToken取得処理完了.")
      Some(Await.result(futureAccessToken, Duration.Inf))
    } else {
      None
    }
  }
}

