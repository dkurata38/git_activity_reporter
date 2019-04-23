package adapter.web.controllers

import java.util.UUID

import application.cache.CacheRepository
import application.inputport.{FindUserByTokenUseCaseInputPort, UserActivationUseCaseInputPort}
import javax.inject.{Inject, Singleton}
import play.api.Configuration
import play.api.mvc.{AbstractController, AnyContent, ControllerComponents, Request}

@Singleton
class SignUpController @Inject() (cc: ControllerComponents, config: Configuration, cacheRepository: CacheRepository, userActivationUseCaseInputPort: UserActivationUseCaseInputPort, findUserByTokenUseCaseInputPort: FindUserByTokenUseCaseInputPort) extends AbstractController(cc){
  def linkGit = Action { implicit request: Request[AnyContent] =>
    request.session.get("accessToken")
      .flatMap(accessToken => findUserByTokenUseCaseInputPort.getUserOf(accessToken))
      .map(_ => Redirect(adapter.web.controllers.routes.SummaryController.index()))
      .getOrElse{
        val accessToken = UUID.randomUUID().toString
        request.session + ("accessToken", accessToken)
        cacheRepository.setCache(accessToken, "oauthPurpose", OauthPurpose.SingUp)
        Ok(views.html.signup.git())
      }
  }

  def complete = Action { implicit request: Request[AnyContent] =>
    request.session.get("accessToken").map{accessToken =>
        userActivationUseCaseInputPort.activate(accessToken) match {
          case Right(token) => Redirect(adapter.web.controllers.routes.SummaryController.index()).withSession(("accessToken", token.value))
          case Left(message) => Redirect(adapter.web.controllers.routes.HomeController.index()).flashing(("message" , message))
        }
    }.getOrElse(Redirect(adapter.web.controllers.routes.HomeController.index()))
  }
}
