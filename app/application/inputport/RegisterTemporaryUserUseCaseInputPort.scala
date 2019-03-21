package application.inputport

import domain.user.User

trait RegisterTemporaryUserUseCaseInputPort {
  def register: User
}
