package adapter.web.controllers

import java.util.UUID
import java.util.concurrent.TimeUnit

import application.cache.CacheRepository
import application.inputport.{CheckRegistrationStatusUseCaseInputPort, LinkGitAccountUseCaseInputPort}
import application.interactor.UserSignInUseCaseInteractor
import domain.git_account.GitClientId.GitHub
import domain.user.RegistrationStatus.{Regular, Temporary}
import javax.inject.{Inject, Singleton}
import play.api.libs.ws.WSClient
import play.api.mvc.{Action, AnyContent, ControllerComponents, Request}
import play.api.{Configuration, Logger}

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

@Singleton
class GithubOAuthController @Inject()(config: Configuration, ws: WSClient, cacheRepository: CacheRepository, cc: ControllerComponents, gitAccountOauthUseCaseInputPort: LinkGitAccountUseCaseInputPort, checkRegistrationStatusUseCaseInputPort: CheckRegistrationStatusUseCaseInputPort, userSignInUseCaseInteractor: UserSignInUseCaseInteractor) extends OAuthController(cacheRepository, cc) {

  val logger = Logger(classOf[GithubOAuthController])

  override def signIn(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    request.session.get("accessToken").map { sessionKey =>
      val gitHubOauth = new GitHubOauth(ws, config)
      cacheRepository.setCache(sessionKey, "gitHubOauth", gitHubOauth, Duration.apply(120, TimeUnit.SECONDS))
      Redirect(gitHubOauth.getAuthorizationURL)
    }.getOrElse(Redirect(adapter.web.controllers.routes.HomeController.index()))
  }

  override def signInCallback(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    val codeOption: Option[String] = request.getQueryString("code")
    val tokenOption = request.session.get("accessToken")
    val stateOption = request.getQueryString("state")
    val gitHubOauthOption = tokenOption.flatMap(sessionKey => cacheRepository.getCache[GitHubOauth](sessionKey, "gitHubOauth"))
    val accessTokenOption = (for {
      code <- codeOption
      state <- stateOption
      gitHubOauth <- gitHubOauthOption
    } yield gitHubOauth.getOAuthAccessToken(state, code)).flatten

    accessTokenOption.map { accessToken =>
      println("accessToken取得成功")
      tokenOption match  {
        case Some(token) => {
          gitAccountOauthUseCaseInputPort.link(token, GitHub, accessToken) match {
            case Right(_) => {
              checkRegistrationStatusUseCaseInputPort.registrationStatus(token) match {
                case Temporary =>  Redirect(adapter.web.controllers.routes.SignUpController.linkSNS())
                case Regular => Redirect(adapter.web.controllers.routes.SummaryController.index())
              }
            }
            case Left(message) => {
              println("サインイン失敗")
              Redirect(adapter.web.controllers.routes.HomeController.index()).flashing(("message", message))
            }
          }
        }
        case _ => userSignInUseCaseInteractor.signInWith(GitHub, accessToken) match {
          case Right(token) => Redirect(adapter.web.controllers.routes.SummaryController.index()).withSession("accessToken" -> token.value)
          case Left(message) => Redirect(adapter.web.controllers.routes.HomeController.index())
        }
      }
    }.getOrElse(Redirect(adapter.web.controllers.routes.HomeController.index()))
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

