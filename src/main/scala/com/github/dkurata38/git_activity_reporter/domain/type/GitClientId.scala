package com.github.dkurata38.git_activity_reporter.domain.`type`

sealed abstract class GitClientId(val value: String)

object GitClientId {
  case object GitHub extends GitClientId("GitHub")
  case object GitLab extends GitClientId("GitLab")
  case object BitBucket extends GitClientId("BitBucket")
}
