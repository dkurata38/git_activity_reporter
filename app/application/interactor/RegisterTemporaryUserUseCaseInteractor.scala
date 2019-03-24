package application.interactor

import application.inputport.RegisterTemporaryUserUseCaseInputPort
import domain.user.{User, UserRepository}
import domain.user_token.{Token, UserToken, UserTokenRepository}
import javax.inject.{Inject, Singleton}

@Singleton
class RegisterTemporaryUserUseCaseInteractor @Inject()(implicit private val userRepository: UserRepository, private val userTokenRepository: UserTokenRepository) extends RegisterTemporaryUserUseCaseInputPort{
  override def register: Token = {
    val user = User.createTemporaryUser
    UserToken.issueTo(user.userId).token
  }
}
