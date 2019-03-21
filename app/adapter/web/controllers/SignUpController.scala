package controllers

import application.cache.{CacheRepository, SignUpCache}
import application.coordinator.UserCoordinator
import application.inputport.UserActivationUseCaseInputPort
import application.repository.IUserRepository
import javax.inject.{Inject, Singleton}
import play.api.Configuration
import play.api.mvc.{AbstractController, AnyContent, ControllerComponents, Request}

@Singleton
class SignUpController @Inject() (cc: ControllerComponents, repo: IUserRepository, config: Configuration, userCoordinator: UserCoordinator, cacheRepository: CacheRepository, userActivationUseCaseInputPort: UserActivationUseCaseInputPort) extends AbstractController(cc){
  def initialize = Action { implicit request: Request[AnyContent] =>
    request.session.get(config.get[String]("session.cookieName")).map { sessionKey =>
      val cacheName = config.get[String]("app.siginup.cache_name")
      cacheRepository.remove(sessionKey, cacheName)

      val user = userCoordinator.registerTemporaryUser
      cacheRepository.setCache(sessionKey, cacheName, SignUpCache.createInstance(user.userId))
      Redirect(routes.SignUpController.linkGit())
    }.getOrElse(Redirect(routes.HomeController.index()).flashing(("message", "セッション有効期限切れ")))
  }

  def linkGit = Action {
    Ok(views.html.signup.git())
  }

  def linkSNS = Action {
    Ok(views.html.signup.sns())
  }

  def complete = Action { implicit request: Request[AnyContent] =>
    request.session.get(config.get[String]("session.cookieName")).flatMap{sessionKey =>
      cacheRepository.getCache[SignUpCache](sessionKey, config.get[String]("app.signin.cache_name")).map{signUpCache =>
        userActivationUseCaseInputPort.activate(signUpCache.userId.value) match {
          case Right(token) => Redirect(routes.SummaryController.index()).withSession(("accessToken", token.value))
          case Left(message) => Redirect(routes.HomeController.index()).flashing(("message" , message))
        }
      }
    }.getOrElse(Redirect(routes.HomeController.index()))
  }
}
