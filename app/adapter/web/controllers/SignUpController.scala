package adapter.web.controllers

import java.util.UUID

import application.cache.CacheRepository
import application.inputport.{FindUserByTokenUseCaseInputPort, UserActivationUseCaseInputPort}
import domain.git_account.GitClientId
import domain.git_account.GitClientId.GitHub
import javax.inject.{Inject, Singleton}
import play.api.Configuration
import play.api.mvc.{AbstractController, AnyContent, ControllerComponents, Request}
import adapter.web.controllers.routes._

@Singleton
class SignUpController @Inject() (cc: ControllerComponents, config: Configuration, cacheRepository: CacheRepository, userActivationUseCaseInputPort: UserActivationUseCaseInputPort, findUserByTokenUseCaseInputPort: FindUserByTokenUseCaseInputPort) extends AbstractController(cc){
  def linkGit(clientId: String) = Action { implicit request: Request[AnyContent] =>
    request.session.get("accessToken")
      .flatMap(accessToken => findUserByTokenUseCaseInputPort.getUserOf(accessToken))
      .map(_ => Redirect(SummaryController.index()))
      .getOrElse{
        val accessToken = UUID.randomUUID().toString
        cacheRepository.setCache(accessToken, "oauthPurpose", OauthPurpose.SignIn)
        val session = request.session + ("accessToken", accessToken)
        GitClientId(clientId) match {
          case GitHub => Redirect(GithubOAuthController.signIn()).withSession(session)
          case _ => Redirect(HomeController.index())
        }
      }
  }

  def complete = Action { implicit request: Request[AnyContent] =>
    request.session.get("accessToken").map{accessToken =>
        userActivationUseCaseInputPort.activate(accessToken) match {
          case Right(token) => Redirect(SummaryController.index()).withSession(("accessToken", token.value))
          case Left(message) => Redirect(HomeController.index()).flashing(("message" , message))
        }
    }.getOrElse(Redirect(HomeController.index()))
  }
}
