package domain.user

case class UserToken(userId: UserId, token: Token) {
  def reIssue(implicit userTokenRepository: UserTokenRepository): UserToken = {
    userTokenRepository.deleteByToken(token)
    UserToken.issueTo(userId)
  }
}

object UserToken{
  def issueTo(userId: UserId)(implicit userTokenRepository: UserTokenRepository): UserToken = {
    val userToken = UserToken(userId, Token.createInstance)
    userTokenRepository.create(userToken)
    userToken
  }
}

