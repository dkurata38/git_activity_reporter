package application.service

import domain.model.user_token.{Token, UserToken, UserTokenRepository}
import javax.inject.{Inject, Singleton}

@Singleton
class UserTokenService @Inject() (userTokenRepository: UserTokenRepository) {
  def getByToken(token: Token) = userTokenRepository.findByUserToken(token)

  def create(userToken: UserToken) = userTokenRepository.create(userToken)
}
