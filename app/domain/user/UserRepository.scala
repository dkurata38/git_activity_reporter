package domain.user

trait UserRepository {
  def create(user: User): Int

  def findOneById(userId: UserId): Option[User]

  def update(user: User): Int
}
