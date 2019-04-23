package application.inputport

import domain.git_account.{AccessToken, GitClientId}
import domain.user_token.Token

trait UserActivationUseCaseInputPort {
  def activate(token: String): Either[String, Token]

  def register(token:String, gitClientId: GitClientId, accessToken: AccessToken): Either[String, Token]
}
