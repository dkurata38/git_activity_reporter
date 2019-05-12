package domain.user

import domain.user.RegistrationStatus.{Regular, Temporary}

class User(val userId: UserId, val registrationStatus: RegistrationStatus) {
  def activate: User = {
    new User(this.userId, Regular)
  }

  def requestAccessToken(implicit userTokenRepository: UserTokenRepository) = UserToken.issueTo(userId)
}

object User {
  def createTemporaryUser(implicit userRepository: UserRepository): User = {
    val user = new User(UserId.newId, Temporary)
    userRepository.create(user)
    user
  }
}
