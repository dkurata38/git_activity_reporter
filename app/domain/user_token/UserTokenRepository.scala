package domain.user_token

import domain.user.UserId

trait UserTokenRepository {
  def findByUserId(userId: UserId): Option[UserToken]

  def findByUserToken(token: Token): Option[UserToken]

  def deleteByToken(token: Token): Int

  def create(userToken: UserToken): Int
}
