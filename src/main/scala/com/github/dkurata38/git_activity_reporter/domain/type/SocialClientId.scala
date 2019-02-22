package com.github.dkurata38.git_activity_reporter.domain.`type`

sealed class SocialClientId(val clientId: String)

object SocialClientId {
  case object Twitter extends SocialClientId("twitter")
}
