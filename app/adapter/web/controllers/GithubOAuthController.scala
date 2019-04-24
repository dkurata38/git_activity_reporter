package adapter.web.controllers

import java.util.UUID
import java.util.concurrent.TimeUnit

import application.cache.CacheRepository
import application.inputport.{FindUserByTokenUseCaseInputPort, LinkGitAccountUseCaseInputPort}
import application.interactor.UserSignInUseCaseInteractor
import domain.git_account.GitClientId.GitHub
import javax.inject.{Inject, Singleton}
import play.api.libs.ws.WSClient
import play.api.mvc.{Action, AnyContent, ControllerComponents, Request}
import play.api.{Configuration, Logger}

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration


@Singleton
class GithubOAuthController @Inject()(implicit config: Configuration, ws: WSClient, cacheRepository: CacheRepository, cc: ControllerComponents, gitAccountOauthUseCaseInputPort: LinkGitAccountUseCaseInputPort, findUserByTokenUseCaseInputPort: FindUserByTokenUseCaseInputPort, userSignInUseCaseInteractor: UserSignInUseCaseInteractor) extends OAuthController(cacheRepository, cc) {

  val logger = Logger(classOf[GithubOAuthController])

  override def signIn(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    val accessToken = request.session.get("accessToken").getOrElse({
      val uuid = UUID.randomUUID().toString
      request.session + ("accessToken", uuid)
      uuid
    })

    val gitHubOauth = new GitHubOauth
    cacheRepository.setCache(accessToken, "gitHubOauth", gitHubOauth, Duration.apply(120, TimeUnit.SECONDS))
    Redirect(gitHubOauth.getAuthorizationURL)
  }

  override def signInCallback(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    request.session.get("accessToken").flatMap{token =>
      val maybeCode: Option[String] = request.getQueryString("code")
      val maybeState = request.getQueryString("state")
      val maybeGitHubOauth = cacheRepository.getCache[GitHubOauth](token, "gitHubOauth")
      val maybeOauthPurpose = cacheRepository.getCache[OauthPurpose](token, "oauthPurpose")
      val maybeAccessToken = (for {
        code <- maybeCode
        state <- maybeState
        gitHubOauth <- maybeGitHubOauth
      } yield gitHubOauth.getOAuthAccessToken(state, code)).flatten

      maybeAccessToken.flatMap { accessToken => maybeOauthPurpose.map {
        case OauthPurpose.SingUp => userSignInUseCaseInteractor.signInWith(GitHub, accessToken) match {
          case Right(userToken) =>
            Redirect(adapter.web.controllers.routes.SummaryController.index()).withSession(("accessToken", userToken.value))
          case Left(message) =>
            Redirect(adapter.web.controllers.routes.HomeController.index()).flashing(("message", message))
        }
        case OauthPurpose.Link => Redirect(adapter.web.controllers.routes.HomeController.index())
      }
    }
  }.getOrElse(Redirect(adapter.web.controllers.routes.HomeController.index()))
}

class GitHubOauth(implicit configuration: Configuration) {

  private val requestToken = UUID.randomUUID().toString
  private val config = OAuthConfig.configLoader.load(configuration.underlying, "app." + GitHub.value)

  def getRequestToken = requestToken

  def getAuthorizationURL =
    s"${GitHubOauth.authorizationUrl}?client_id=${config.clientId}&client_secret=${config.clientSecret}&redirect_url=${config.callbackUrl}&scope=${GitHubOauth.scope}&state=$requestToken"

  def getOAuthAccessToken(state: String, code: String)(implicit ws: WSClient) = {
    if (state == requestToken) {
      val futureAccessToken = ws.url(GitHubOauth.accessTokenUrl)
        .addHttpHeaders(("Accept", "application/json"))
        .addQueryStringParameters(("client_id", config.clientId), ("client_secret", config.clientSecret), ("code", code))
        .withRequestTimeout(Duration.apply(10000, TimeUnit.MILLISECONDS))
        .get().map(response => (response.json \ "access_token").as[String])
      println("AccessToken取得処理完了.")
      Some(Await.result(futureAccessToken, Duration.Inf))
    } else {
      None
    }
  }
}
object GitHubOauth{
  private val authorizationUrl = "https://github.com/login/oauth/authorize"
  private val accessTokenUrl = "https://github.com/login/oauth/access_token"
  private val scope = "repo"
}

