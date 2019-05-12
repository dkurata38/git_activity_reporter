package adapter.gateway.github

import domain.git.account.{AccessToken, GitAccount}
import domain.git_account.GitAccount

trait GitHubUserGateway {
  def getUser(accessToken: AccessToken): Option[GitAccount]
}
