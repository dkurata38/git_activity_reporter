package com.github.dkurata38.git_activity_reporter.application.repository

import com.github.dkurata38.git_activity_reporter.domain.model.social.SocialAccount

trait ISocialAccountRepository {
  def findAllByUserId(userId: Int): Seq[SocialAccount]

  def findOneByUserIdAndSocialProviderId(userId: Int): Option[SocialAccount]
}
