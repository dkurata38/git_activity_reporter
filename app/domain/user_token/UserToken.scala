package domain.user_token

import domain.user.UserId

case class UserToken(userId: UserId, token: Token) {
  def reIssue(implicit userTokenRepository: UserTokenRepository) = {
    userTokenRepository.deleteByToken(token)
    UserToken.issueTo(userId)
  }
}

object UserToken{
  def issueTo(userId: UserId)(implicit userTokenRepository: UserTokenRepository) = {
    val userToken = UserToken(userId, Token.createInstance)
    userTokenRepository.create(userToken)
    userToken
  }
}

