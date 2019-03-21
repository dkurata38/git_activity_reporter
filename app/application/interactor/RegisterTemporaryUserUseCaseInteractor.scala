package application.interactor

import application.inputport.RegisterTemporaryUserUseCaseInputPort
import domain.user.{User, UserRepository}
import javax.inject.{Inject, Singleton}

@Singleton
class RegisterTemporaryUserUseCaseInteractor @Inject()(implicit private val userRepository: UserRepository) extends RegisterTemporaryUserUseCaseInputPort{
  override def register: User = User.createTemporaryUser
}
