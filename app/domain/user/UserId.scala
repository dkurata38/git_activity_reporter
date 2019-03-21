package domain.user

import java.util.UUID

case class UserId(value: String)

object UserId {
  def newId: UserId = UserId(UUID.randomUUID().toString)
}
