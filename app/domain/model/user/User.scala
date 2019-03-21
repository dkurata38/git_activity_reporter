package domain.model.user

import application.repository.{IUserRepository, IUserTokenRepository}
import domain.model.user.RegistrationStatus.{Regular, Temporary}
import domain.model.user_token.UserToken

class User(val userId: UserId, val registrationStatus: RegistrationStatus) {
  def activate: User = {
    new User(this.userId, Regular)
  }

  def requestAccessToken(implicit userTokenRepository: IUserTokenRepository) = UserToken.issueTo(userId)
}

object User {
  def createTemporaryUser(implicit userRepository: IUserRepository): User = {
    val user = User(UserId.newId, Temporary)
    userRepository.create(user)
    user
  }
}
