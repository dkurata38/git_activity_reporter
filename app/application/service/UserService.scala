package application.service

import application.repository.IUserRepository
import domain.model.user.{User, UserId}
import javax.inject.{Inject, Singleton}

@Singleton
class UserService @Inject()(private val repository: IUserRepository) {
  def createUser(user: User): Int = repository.create(user)

  def getById(userId: UserId): Option[User] = repository.findOneById(userId)

  def updateUser(user: User): Int = repository.update(user)
}
