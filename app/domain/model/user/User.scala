package domain.model.user

import domain.model.user.RegistrationStatus.{Regular, Temporary}
import domain.model.user_token.{UserToken, UserTokenRepository}

class User(val userId: UserId, val registrationStatus: RegistrationStatus) {
  def activate: User = {
    new User(this.userId, Regular)
  }

  def requestAccessToken(implicit userTokenRepository: UserTokenRepository) = UserToken.issueTo(userId)
}

object User {
  def createTemporaryUser(implicit userRepository: UserRepository): User = {
    val user = User(UserId.newId, Temporary)
    userRepository.create(user)
    user
  }
}
