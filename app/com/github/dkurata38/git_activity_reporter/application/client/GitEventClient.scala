package com.github.dkurata38.git_activity_reporter.application.client

import com.github.dkurata38.git_activity_reporter.domain.model.git.{GitAccount, GitEvents}

trait GitEventClient {
  def getUserEvents(gitAccount: GitAccount): GitEvents
}
