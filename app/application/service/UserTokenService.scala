package application.service

import application.repository.IUserTokenRepository
import domain.model.user_token.{Token, UserToken}
import javax.inject.{Inject, Singleton}

@Singleton
class UserTokenService @Inject() (userTokenRepository: IUserTokenRepository) {
  def getByToken(token: Token) = userTokenRepository.findByUserToken(token)

  def create(userToken: UserToken) = userTokenRepository.create(userToken)
}
