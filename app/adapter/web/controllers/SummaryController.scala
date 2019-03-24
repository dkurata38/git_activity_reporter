package adapter.web.controllers

import application.inputport.GitActivityQueryUseCaseInputPort
import javax.inject.{Inject, Singleton}
import play.api.Configuration
import play.api.cache.SyncCacheApi
import play.api.mvc.{AbstractController, AnyContent, ControllerComponents, Request}

@Singleton
class SummaryController @Inject() (cc:ControllerComponents, gitActivityQueryUseCaseInputPort: GitActivityQueryUseCaseInputPort, cache: SyncCacheApi, config: Configuration) extends AbstractController(cc){
  def index = Action { implicit request: Request[AnyContent] =>
    request.session.get("accessToken").map{accessToken =>
      val gitEvents = gitActivityQueryUseCaseInputPort.queryGitActivities(accessToken)
      Ok(views.html.summary.index(gitEvents))
    }.getOrElse(Redirect(adapter.web.controllers.routes.HomeController.index()))
  }
}
