package application.service

import application.repository.IUserRepository
import domain.model.user.User
import javax.inject.{Inject, Singleton}

@Singleton
class UserService @Inject()(private val repository: IUserRepository) {
  def registerUser(user: User): User = repository.create(user)
}
