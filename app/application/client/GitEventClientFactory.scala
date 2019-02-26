package application.client

import domain.`type`.GitClientId
import domain.`type`.GitClientId.GitHub
import infrastracture.client.git_event.github.GitHubEventClient

class GitEventClientFactory {
  def getInstance(clientId: GitClientId): GitEventClient = {
    clientId match {
      case GitHub => new GitHubEventClient
    }
  }
}
