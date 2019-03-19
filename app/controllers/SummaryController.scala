package controllers

import application.coordinator.GitEventsCoordinator
import application.service.UserTokenService
import domain.model.user_token.Token
import javax.inject.{Inject, Singleton}
import play.api.Configuration
import play.api.cache.SyncCacheApi
import play.api.mvc.{AbstractController, AnyContent, ControllerComponents, Request}

@Singleton
class SummaryController @Inject() (cc:ControllerComponents, gitActivitySummaryCoordinator: GitEventsCoordinator, cache: SyncCacheApi, config: Configuration, userTokenService: UserTokenService) extends AbstractController(cc){
  def index = Action { implicit request: Request[AnyContent] =>
    request.session.get("accessToken").flatMap{accessToken =>
      userTokenService.getByToken(Token(accessToken)).map{userToken =>
        val gitEvents = gitActivitySummaryCoordinator.getGitEvents(userToken.userId)
        Ok(views.html.summary.index(gitEvents))
      }
    }.getOrElse(Redirect(routes.HomeController.index()))
  }
}
