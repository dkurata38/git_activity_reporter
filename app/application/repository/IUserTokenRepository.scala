package application.repository

import domain.model.user.UserId
import domain.model.user_token.{Token, UserToken}

trait IUserTokenRepository {
  def findByUserId(userId: UserId): Option[UserToken]

  def findByUserToken(token: Token): Option[UserToken]

  def deleteByToken(token: Token): Int

  def create(userToken: UserToken): Int
}
