package application.service

import application.repository.ISocialAccountRepository
import domain.`type`.SocialClientId
import domain.model.social.{SocialAccount, SocialAccountId}
import domain.model.user.UserId
import javax.inject.{Inject, Singleton}

@Singleton
class SocialAccountService @Inject()(private val repository: ISocialAccountRepository) {
  def getAllByUserId(userId: UserId) = repository.findAllByUserId(userId)

  def getByUserIdClientId(userId: UserId, clientId: SocialClientId) = repository.findOneByUserIdAndSocialClientId(userId, clientId)

  def getBySocialAccountId(clientId: SocialClientId, socialAccountId: SocialAccountId) = repository.findOneBySocialAccountId(clientId, socialAccountId)

  def link(socialAccount: SocialAccount): SocialAccount = repository.create(socialAccount)
}
