package com.github.dkurata38.git_activity_reporter.application.client

import com.github.dkurata38.git_activity_reporter.domain.`type`.GitClientId
import com.github.dkurata38.git_activity_reporter.domain.`type`.GitClientId.GitHub
import com.github.dkurata38.git_activity_reporter.infrastracture.client.git_event.github.GitHubEventClient

class GitEventClientFactory {
  def getInstance(clientId: GitClientId) :GitEventClient = {
    clientId match {
      case GitHub => new GitHubEventClient
    }
  }
}
