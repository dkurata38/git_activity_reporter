package application.inputport

import domain.user.RegistrationStatus

trait CheckRegistrationStatusUseCaseInputPort {
  def registrationStatus(token: String): RegistrationStatus
}
