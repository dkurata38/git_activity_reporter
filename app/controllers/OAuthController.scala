package controllers

import application.cache.CacheRepository
import play.api.mvc._

abstract class OAuthController(cacheRepository: CacheRepository, cc: ControllerComponents) extends AbstractController(cc) {
  def signIn(): Action[AnyContent]

  def signInCallback(): Action[AnyContent]
}
