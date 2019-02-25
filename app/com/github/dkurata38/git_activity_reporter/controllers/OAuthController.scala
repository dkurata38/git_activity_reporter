package com.github.dkurata38.git_activity_reporter.controllers

import play.api.cache.SyncCacheApi
import play.api.mvc._

abstract class OAuthController (cache: SyncCacheApi, cc: ControllerComponents) extends AbstractController(cc) {
  def signIn(): Action[AnyContent]

  def oauthCallback(): Action[AnyContent]
}
