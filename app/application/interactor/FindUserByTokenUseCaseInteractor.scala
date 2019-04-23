package application.interactor

import application.inputport.FindUserByTokenUseCaseInputPort
import domain.user.{RegistrationStatus, User, UserRepository}
import domain.user_token.{Token, UserTokenRepository}
import javax.inject.{Inject, Singleton}

@Singleton
class FindUserByTokenUseCaseInteractor @Inject()(userTokenRepository: UserTokenRepository, userRepository: UserRepository) extends FindUserByTokenUseCaseInputPort {
  override def registrationStatus(token: String): RegistrationStatus = {
    userTokenRepository.findByUserToken(Token(token)).flatMap(userToken =>
      userRepository.findOneById(userToken.userId).map(user => user.registrationStatus)
    )
  }.orNull

  override def getUserOf(token: String): Option[User] =
    userTokenRepository.findByUserToken(Token(token)).flatMap(userToken =>
      userRepository.findOneById(userToken.userId)
    )
}
