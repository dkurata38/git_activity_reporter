package controllers

import javax.inject.{Inject, Singleton}
import play.api.cache.SyncCacheApi
import play.api.mvc.{AbstractController, ControllerComponents}

@Singleton
class SignUpController @Inject() (cc: ControllerComponents, cache: SyncCacheApi) extends AbstractController(cc){
  def linkGit = Action {
    cache.get("signupcache")
    Ok(views.html.signup.git())
  }

  def linkSNS = Action {
    Ok(views.html.signup.sns())
  }

  def complete = Action {
    Ok(views.html.summary.index())
  }
}
