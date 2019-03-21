package adapter.gateway.github

import domain.model.git.account.{AccessToken, GitAccount}

trait GitHubUserGateway {
  def getUser(accessToken: AccessToken): GitAccount
}
