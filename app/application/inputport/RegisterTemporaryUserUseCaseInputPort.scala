package application.inputport

import domain.model.user.User

trait RegisterTemporaryUserUseCaseInputPort {
  def register: User
}
