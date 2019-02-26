package infrastracture.repository

import application.repository.IUserRepository
import domain.model.user.User
import javax.inject.{Inject, Singleton}

@Singleton
class UserRepository @Inject() extends IUserRepository {
  override def create(user: User): User = ???
}
