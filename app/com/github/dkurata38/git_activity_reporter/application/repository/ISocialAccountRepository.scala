package com.github.dkurata38.git_activity_reporter.application.repository

import com.github.dkurata38.git_activity_reporter.domain.`type`.SocialClientId
import com.github.dkurata38.git_activity_reporter.domain.model.social.{SocialAccount, SocialAccountId}
import com.github.dkurata38.git_activity_reporter.domain.model.user.UserId

trait ISocialAccountRepository {
  def findAllByUserId(userId: UserId): Seq[SocialAccount]

  def findOneByUserIdAndSocialClientId(userId: UserId, clientId: SocialClientId): Option[SocialAccount]

  def findOneBySocialAccountId(clientId: SocialClientId, accountId: SocialAccountId): Option[SocialAccount]

  def create(socialAccount: SocialAccount): SocialAccount
}
