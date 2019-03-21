package application.inputport

import domain.model.user_token.Token

trait UserActivationUseCaseInputPort {
  def activate(userId: String): Either[String, Token]
}
