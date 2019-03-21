package adapter.gateway.github

import domain.git_account.{AccessToken, GitAccount}

trait GitHubUserGateway {
  def getUser(accessToken: AccessToken): Option[GitAccount]
}
