package domain.model.user

import domain.model.user.RegistrationStatus.{Regular, Temporary}

class User(val userId: UserId, val registrationStatus: RegistrationStatus) {
  def activate: User = {
    new User(this.userId, Regular)
  }
}

object User {
  def createInstance: User = {
    new User(UserId.newId, Temporary)
  }
}
