package application.inputport

import domain.user.{RegistrationStatus, User}

trait FindUserByTokenUseCaseInputPort {
  def registrationStatus(token: String): RegistrationStatus

  def getUserOf(token: String): Option[User]
}
