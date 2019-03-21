package adapter.gateway

import domain.model.git.account.{AccessToken, GitAccount}

trait GitHubUserGateway {
  def getUser(accessToken: AccessToken): GitAccount
}
