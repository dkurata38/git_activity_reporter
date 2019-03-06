package controllers

import application.cache.SignInCache
import application.coordinator.GitEventsCoordinator
import domain.model.git.account.GitClientId.GitHub
import domain.model.social.SocialClientId.Twitter
import javax.inject.{Inject, Singleton}
import play.api.Configuration
import play.api.cache.SyncCacheApi
import play.api.mvc.{AbstractController, AnyContent, ControllerComponents, Request}

@Singleton
class SummaryController @Inject() (cc:ControllerComponents, gitActivitySummaryCoordinator: GitEventsCoordinator, cache: SyncCacheApi, config: Configuration) extends AbstractController(cc){
  def index = Action { implicit request: Request[AnyContent] =>
    val signInCacheOption: Option[SignInCache] = cache.get[SignInCache](config.get[String]("app.signin.cache_name"))
    signInCacheOption.map { signInCache =>
      println(s"${signInCache.user.registrationStatus.value} ${signInCache.getGitAccessTokenByClientId(GitHub).orNull} ${signInCache.getSocialAccessTokenByClientId(Twitter).map(t => t.toString).orNull}")
      val gitEvents = gitActivitySummaryCoordinator.getGitEvents(signInCache.user.userId)
      Ok(views.html.summary.index(gitEvents))
    }.getOrElse(Redirect(routes.HomeController.index()))
  }
}
