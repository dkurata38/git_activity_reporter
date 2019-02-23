package com.github.dkurata38.git_activity_reporter.infrastracture.repository.social_account

import com.github.dkurata38.git_activity_reporter.application.repository.ISocialAccountRepository
import com.github.dkurata38.git_activity_reporter.domain.`type`.SocialClientId.Twitter
import com.github.dkurata38.git_activity_reporter.domain.model.social.SocialAccount
import com.typesafe.config.ConfigFactory

class SocialAccountRepository extends ISocialAccountRepository {
  override def findAllByUserId(userId: Int): Seq[SocialAccount] = {
    val config = ConfigFactory.load()
    Seq(new SocialAccount(1111, Twitter, config.getString("app.twitter.access_token"), config.getString("app.twitter.access_token_secret")))
  }

  override def findOneByUserIdAndSocialProviderId(userId: Int): Option[SocialAccount] = ???
}
