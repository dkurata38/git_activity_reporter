package controllers

import application.cache.{SignInCache, SignUpCache}
import application.coordinator.UserCoordinator
import application.repository.IUserRepository
import javax.inject.{Inject, Singleton}
import play.api.Configuration
import play.api.cache.SyncCacheApi
import play.api.mvc.{AbstractController, AnyContent, ControllerComponents, Request}

@Singleton
class SignUpController @Inject() (cc: ControllerComponents, cache: SyncCacheApi, repo: IUserRepository, config: Configuration, userCoordinator: UserCoordinator) extends AbstractController(cc){
  def initialize = Action { implicit request: Request[AnyContent] =>
    cache.remove(config.get[String]("app.signin.cache_name"))
    val user = userCoordinator.registerNewUser
    cache.set(config.get[String]("app.signin.cache_name"), new SignInCache(user))
    Redirect(routes.SignUpController.linkGit())
  }

  def linkGit = Action {
    Ok(views.html.signup.git())
  }

  def linkSNS = Action {
    Ok(views.html.signup.sns())
  }

  def complete = Action {

    Redirect(routes.SummaryController.index())
  }
}
