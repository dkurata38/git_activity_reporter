package application.repository

import domain.model.user.User

trait IUserRepository {
  def create(user: User): Int
}
