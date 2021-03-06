package domain.user

import java.util.UUID

case class Token (value: String) {
  override def toString: String = value
}

object Token{
  def createInstance = Token(UUID.randomUUID().toString)
}