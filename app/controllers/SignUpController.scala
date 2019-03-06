package controllers

import application.cache.{CacheRepository, SignUpCache}
import application.coordinator.UserCoordinator
import application.repository.IUserRepository
import javax.inject.{Inject, Singleton}
import play.api.Configuration
import play.api.mvc.{AbstractController, AnyContent, ControllerComponents, Request}

@Singleton
class SignUpController @Inject() (cc: ControllerComponents, repo: IUserRepository, config: Configuration, userCoordinator: UserCoordinator, cacheRepository: CacheRepository) extends AbstractController(cc){
  def initialize = Action { implicit request: Request[AnyContent] =>
    request.session.get(config.get[String]("session.cookieName")).map { sessionKey =>
      val cacheName = config.get[String]("app.siginup.cache_name")
      cacheRepository.remove(sessionKey, cacheName)
      cacheRepository.setCache(sessionKey, cacheName, SignUpCache.createInstance)
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
      cacheRepository.getCache[SignUpCache](sessionKey, config.get[String]("app.signin.cache_name")).flatMap{signUpCache =>
        val accessTokenOption = userCoordinator.activateUser(signUpCache)
        accessTokenOption.map(accessToken => Redirect(routes.SummaryController.index()).withSession(("accessToken", accessToken)))
      }
    }getOrElse(Redirect(routes.HomeController.index()))
  }
}
