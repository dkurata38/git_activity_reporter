package com.github.dkurata38.git_activity_reporter.application.client

import com.github.dkurata38.git_activity_reporter.domain.git_account.GitAccount
import com.github.dkurata38.git_activity_reporter.domain.git_event.GitEvent

trait GitEventClient {
  def getUserEvents(gitAccount: GitAccount): Seq[GitEvent]
}
