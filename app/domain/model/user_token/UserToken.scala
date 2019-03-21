package domain.model.user_token

import application.repository.IUserTokenRepository
import domain.model.user.UserId

case class UserToken(userId: UserId, token: Token) {
  def reIssue(implicit userTokenRepository: IUserTokenRepository) = {
    userTokenRepository.deleteByToken(token)
    UserToken.issueTo(userId)
  }
}

object UserToken{
  def issueTo(userId: UserId)(implicit userTokenRepository: IUserTokenRepository) = {
    val userToken = UserToken(userId, Token.createInstance)
    userTokenRepository.create(userToken)
    userToken
  }
}

