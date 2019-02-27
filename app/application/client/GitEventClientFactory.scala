package application.client

import domain.model.git.account.GitClientId
import domain.model.git.account.GitClientId.GitHub
import infrastracture.client.git.github.GitHubClient

class GitEventClientFactory {
  def getInstance(clientId: GitClientId): GitClient = {
    clientId match {
      case GitHub => new GitHubClient
      case _ => throw new IllegalArgumentException
    }
  }
}
