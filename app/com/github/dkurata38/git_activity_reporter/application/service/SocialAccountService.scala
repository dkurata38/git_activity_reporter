package com.github.dkurata38.git_activity_reporter.application.service

import com.github.dkurata38.git_activity_reporter.application.repository.ISocialAccountRepository
import com.github.dkurata38.git_activity_reporter.domain.`type`.SocialClientId
import com.github.dkurata38.git_activity_reporter.domain.model.social.{SocialAccount, SocialAccountId}
import com.github.dkurata38.git_activity_reporter.domain.model.user.UserId
import javax.inject.{Inject, Singleton}

@Singleton
class SocialAccountService @Inject() (private val repository: ISocialAccountRepository) {
  def getAllByUserId(userId: UserId) = repository.findAllByUserId(userId)

  def getByUserIdClientId(userId: UserId, clientId: SocialClientId) = repository.findOneByUserIdAndSocialClientId(userId, clientId)

  def getBySocialAccountId(clientId: SocialClientId, socialAccountId: SocialAccountId) = repository.findOneBySocialAccountId(clientId, socialAccountId)

  def link(socialAccount: SocialAccount): SocialAccount = repository.create(socialAccount)
}
