package application.service

import domain.social.{SocialAccount, SocialAccountId, SocialAccountRepository, SocialClientId}
import domain.user.UserId
import javax.inject.{Inject, Singleton}

@Singleton
class SocialAccountService @Inject()(private val repository: SocialAccountRepository) {
  def getAllByUserId(userId: UserId) = repository.findAllByUserId(userId)

  def getBySocialAccountId(clientId: SocialClientId, socialAccountId: SocialAccountId) = repository.findOneBySocialAccountId(clientId, socialAccountId)

  def link(socialAccount: SocialAccount): Int = repository.create(socialAccount)
}
