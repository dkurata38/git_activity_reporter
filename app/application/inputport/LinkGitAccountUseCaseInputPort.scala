package application.inputport

import domain.git_account.{GitAccount, GitClientId}

trait LinkGitAccountUseCaseInputPort {
  /**
    *
    * @param clientId
    * @param accessToken
    * @return
    */
  def link(token: String, clientId: GitClientId, accessToken: String): Either[String, GitAccount]
}
