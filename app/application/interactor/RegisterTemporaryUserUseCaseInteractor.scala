package application.interactor

import application.inputport.RegisterTemporaryUserUseCaseInputPort
import application.repository.IUserRepository
import domain.model.user.User
import javax.inject.{Inject, Singleton}

@Singleton
class RegisterTemporaryUserUseCaseInteractor @Inject()(implicit private val userRepository: IUserRepository) extends RegisterTemporaryUserUseCaseInputPort{
  override def register: User = User.createTemporaryUser
}
