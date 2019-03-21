package application.service

import domain.model.social.{SocialAccountRepository, SocialAccount, SocialAccountId, SocialClientId}
import domain.model.user.UserId
import javax.inject.{Inject, Singleton}

@Singleton
class SocialAccountService @Inject()(private val repository: SocialAccountRepository) {
  def getAllByUserId(userId: UserId) = repository.findAllByUserId(userId)

  def getBySocialAccountId(clientId: SocialClientId, socialAccountId: SocialAccountId) = repository.findOneBySocialAccountId(clientId, socialAccountId)

  def link(socialAccount: SocialAccount): Int = repository.create(socialAccount)
}
