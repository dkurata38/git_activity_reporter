package application.inputport

import domain.git_account.{AccessToken, GitAccount, GitClientId}

trait LinkGitAccountUseCaseInputPort {
  /**
    *
    * @param clientId
    * @param accessToken
    * @return
    */
  def link(token: String, clientId: GitClientId, accessToken: AccessToken): Either[String, GitAccount]
}
