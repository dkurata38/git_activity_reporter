package com.github.dkurata38.git_activity_reporter.controllers

import com.github.dkurata38.git_activity_reporter.application.coordinator.GitEventsCoordinator
import javax.inject.{Inject, Singleton}
import play.api.cache.SyncCacheApi
import play.api.mvc.{AbstractController, AnyContent, ControllerComponents, Request}

@Singleton
class SummaryController @Inject() (cc:ControllerComponents, cache: SyncCacheApi, coordinator: GitEventsCoordinator) extends AbstractController(cc){
  def index = Action { implicit request: Request[AnyContent] =>
    cache.get[SignInCache]("signInCache").map { signInCache =>
      val gitEvents = coordinator.getGitEvents(signInCache.userId)
      Ok(views.html.summary.index(gitEvents))
    }.getOrElse(Redirect(routes.HomeController.index()))
  }
}
