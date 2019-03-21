package application.service

import domain.model.user.{User, UserId, UserRepository}
import javax.inject.{Inject, Singleton}

@Singleton
class UserService @Inject()(private val repository: UserRepository) {
  def create(user: User) = repository.create(user)

  def getById(userId: UserId): Option[User] = repository.findOneById(userId)

  def updateUser(user: User): Int = repository.update(user)
}
