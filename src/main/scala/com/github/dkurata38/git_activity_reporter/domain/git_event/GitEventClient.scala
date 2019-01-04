package com.github.dkurata38.git_activity_reporter.domain.git_event

import com.github.dkurata38.git_activity_reporter.domain.git_account.GitAccount

trait GitEventClient {
  def getUserEvents(gitAccount: GitAccount): Seq[GitEvent]
}
