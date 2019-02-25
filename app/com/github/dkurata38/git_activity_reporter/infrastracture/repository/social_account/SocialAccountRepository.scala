package com.github.dkurata38.git_activity_reporter.infrastracture.repository.social_account

import com.github.dkurata38.git_activity_reporter.application.repository.ISocialAccountRepository
import com.github.dkurata38.git_activity_reporter.domain.`type`.SocialClientId
import com.github.dkurata38.git_activity_reporter.domain.`type`.SocialClientId.Twitter
import com.github.dkurata38.git_activity_reporter.domain.model.social.{SocialAccessToken, SocialAccount, SocialAccountId}
import com.github.dkurata38.git_activity_reporter.domain.model.user.UserId
import com.typesafe.config.ConfigFactory
import javax.inject.{Inject, Singleton}

@Singleton
class SocialAccountRepository @Inject() extends ISocialAccountRepository {
  override def findAllByUserId(userId: UserId): Seq[SocialAccount] = {
    val config = ConfigFactory.load()
    Seq(new SocialAccount(UserId("1111"), Twitter, SocialAccountId(""), SocialAccessToken(config.getString("app.twitter.access_token"), config.getString("app.twitter.access_token_secret"))))
  }

  override def findOneByUserIdAndSocialClientId(userId: UserId, clientId: SocialClientId): Option[SocialAccount] = ???

  override def findOneBySocialAccountId(clientId: SocialClientId, accountId: SocialAccountId): Option[SocialAccount] = ???

  override def create(socialAccount: SocialAccount): SocialAccount = ???
}
