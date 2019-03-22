package application.interactor

import application.inputport.CheckRegistrationStatusUseCaseInputPort
import domain.user.{RegistrationStatus, UserRepository}
import domain.user_token.{Token, UserTokenRepository}
import javax.inject.{Inject, Singleton}

@Singleton
class CheckRegistrationStatusUseCaseInteractor @Inject()(userTokenRepository: UserTokenRepository, userRepository: UserRepository) extends CheckRegistrationStatusUseCaseInputPort {
  override def registrationStatus(token: String): RegistrationStatus = {
    userTokenRepository.findByUserToken(Token(token)).flatMap(userToken =>
      userRepository.findOneById(userToken.userId).map(user => user.registrationStatus)
    )
  }.orNull
}
