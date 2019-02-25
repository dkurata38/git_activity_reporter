package com.github.dkurata38.git_activity_reporter.domain.model.user

import java.util.UUID

case class UserId(value: String)

object UserId {
  def newId: UserId = UserId(UUID.randomUUID().toString)
}
