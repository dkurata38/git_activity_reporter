package application.repository

import domain.model.user.{User, UserId}

trait IUserRepository {
  def create(user: User): Int

  def findOneById(userId: UserId): Option[User]

  def update(user: User): Int
}
