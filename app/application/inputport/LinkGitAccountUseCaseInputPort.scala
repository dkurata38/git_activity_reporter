package application.inputport

import domain.model.git.account.{GitAccount, GitClientId}

trait LinkGitAccountUseCaseInputPort {
  /**
    *
    * @param clientId
    * @param accessToken
    * @return
    */
  def link(token: String, clientId: GitClientId, accessToken: String): Either[String, GitAccount]
}
