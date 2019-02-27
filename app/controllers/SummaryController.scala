package controllers

import application.coordinator.GitEventsCoordinator
import javax.inject.{Inject, Singleton}
import play.api.Configuration
import play.api.cache.SyncCacheApi
import play.api.mvc.{AbstractController, AnyContent, ControllerComponents, Request}

@Singleton
class SummaryController @Inject() (cc:ControllerComponents, gitActivitySummaryCoordinator: GitEventsCoordinator, cache: SyncCacheApi, config: Configuration) extends AbstractController(cc){
  def index = Action { implicit request: Request[AnyContent] =>
    cache.get[SignInCache]("signInCache").map { signInCache =>
      val gitEvents = gitActivitySummaryCoordinator.getGitEvents(signInCache.userId)
      Ok(views.html.summary.index(gitEvents))
    }.getOrElse(Redirect(routes.HomeController.index()))
  }
}
