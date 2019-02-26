package domain.model.user

class User(val userId: UserId)

object User {
  def newUser: User = {
    val userId = UserId.newId
    new User(userId)
  }
}
