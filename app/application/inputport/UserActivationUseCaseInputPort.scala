package application.inputport

import domain.git.GitClientId
import domain.git.account.AccessToken
import domain.user.Token

trait UserActivationUseCaseInputPort {
  def activate(token: String): Either[String, Token]

  def register(token:String, gitClientId: GitClientId, accessToken: AccessToken): Either[String, Token]
}
