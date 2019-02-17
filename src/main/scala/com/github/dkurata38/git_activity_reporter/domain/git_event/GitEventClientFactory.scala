package com.github.dkurata38.git_activity_reporter.domain.git_event

import com.github.dkurata38.git_activity_reporter.domain.git_account.GitAccount.ClientId
import com.github.dkurata38.git_activity_reporter.domain.git_account.GitAccount.ClientId.GitHub
import com.github.dkurata38.git_activity_reporter.infrastracture.client.git_event.github.GitHubEventClient

class GitEventClientFactory {
  def getInstance(clientId: ClientId) :GitEventClient = {
    clientId match {
      case GitHub => new GitHubEventClient
    }
  }
}
