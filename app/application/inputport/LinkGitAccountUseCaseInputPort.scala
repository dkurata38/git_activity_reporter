package application.inputport

import domain.git.account.{AccessToken, GitAccount}
import domain.git.GitClientId
import domain.git_account.GitAccount

trait LinkGitAccountUseCaseInputPort {
  /**
    *
    * @param clientId
    * @param accessToken
    * @return
    */
  def link(token: String, clientId: GitClientId, accessToken: AccessToken): Either[String, GitAccount]
}
