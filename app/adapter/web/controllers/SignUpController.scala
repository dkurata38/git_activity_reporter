package adapter.web.controllers

import application.cache.CacheRepository
import application.inputport.{RegisterTemporaryUserUseCaseInputPort, UserActivationUseCaseInputPort}
import domain.user.UserRepository
import javax.inject.{Inject, Singleton}
import play.api.Configuration
import play.api.mvc.{AbstractController, AnyContent, ControllerComponents, Request}

@Singleton
class SignUpController @Inject() (cc: ControllerComponents, repo: UserRepository, config: Configuration, cacheRepository: CacheRepository, userActivationUseCaseInputPort: UserActivationUseCaseInputPort, registerTemporaryUserUseCaseInputPort: RegisterTemporaryUserUseCaseInputPort) extends AbstractController(cc){
  def initialize = Action { implicit request: Request[AnyContent] =>
      val token = registerTemporaryUserUseCaseInputPort.register
      Redirect(adapter.web.controllers.routes.SignUpController.linkGit()).withSession("accessToken" -> token.value)
  }

  def linkGit = Action {
    Ok(views.html.signup.git())
  }

  def linkSNS = Action {
    Ok(views.html.signup.sns())
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
