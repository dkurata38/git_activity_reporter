package domain.model.user_token

import domain.model.user.UserId

case class UserToken(userId: UserId, token: Token)

object UserToken{
  def issueTo(userId: UserId) = UserToken(userId, Token.createInstance)
}

