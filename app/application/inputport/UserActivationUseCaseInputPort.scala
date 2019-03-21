package application.inputport

import domain.user_token.Token

trait UserActivationUseCaseInputPort {
  def activate(userId: String): Either[String, Token]
}
