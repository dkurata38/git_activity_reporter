package com.github.dkurata38.git_activity_reporter.application.repository

import com.github.dkurata38.git_activity_reporter.domain.model.social.SocialAccount
import com.github.dkurata38.git_activity_reporter.domain.model.user.UserId

trait ISocialAccountRepository {
  def findAllByUserId(userId: UserId): Seq[SocialAccount]

  def findOneByUserIdAndSocialProviderId(userId: UserId): Option[SocialAccount]
}
