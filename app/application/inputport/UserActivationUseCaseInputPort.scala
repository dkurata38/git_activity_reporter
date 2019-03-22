package application.inputport

import domain.user_token.Token

trait UserActivationUseCaseInputPort {
  def activate(token: String): Either[String, Token]
}
