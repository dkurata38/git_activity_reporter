package application.inputport

import domain.user_token.Token

trait RegisterTemporaryUserUseCaseInputPort {
  def register: Token
}
