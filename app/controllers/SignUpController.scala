package controllers

import application.cache.SignUpCache
import application.coordinator.UserCoordinator
import application.repository.IUserRepository
import javax.inject.{Inject, Singleton}
import play.api.Configuration
import play.api.cache.SyncCacheApi
import play.api.mvc.{AbstractController, AnyContent, ControllerComponents, Request}

@Singleton
class SignUpController @Inject() (cc: ControllerComponents, cache: SyncCacheApi, repo: IUserRepository, config: Configuration, userCoordinator: UserCoordinator) extends AbstractController(cc){
  def initialize = Action { implicit request: Request[AnyContent] =>
    cache.remove(config.get[String]("app.signup.cache_name"))
    val userId = userCoordinator.registerNewUser
    cache.set(config.get[String]("app.signup.cache_name"), new SignUpCache(userId, None, None))
    Redirect(routes.SignUpController.linkGit())
  }

  def linkGit = Action {
//    repo.create(new GitAccount(UserId("1111"), GitHub, "aaaa", "testtoken"))
    Ok(views.html.signup.git())
  }

  def linkSNS = Action {
    Ok(views.html.signup.sns())
  }

  def complete = Action {

    Redirect(routes.SummaryController.index())
  }
}
