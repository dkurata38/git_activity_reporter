package application.client

import domain.model.git.account.{AccessToken, GitAccount}
import domain.model.git.event.GitEvents

trait GitClient {
  def getUserEvents(gitAccount: GitAccount): GitEvents

  def getAuthenticatedUser(accessToken: AccessToken): GitAccount
}
