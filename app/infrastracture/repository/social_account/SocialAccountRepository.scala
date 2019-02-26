package infrastracture.repository.social_account

import application.repository.ISocialAccountRepository
import domain.`type`.SocialClientId
import domain.`type`.SocialClientId.Twitter
import domain.model.social.{SocialAccessToken, SocialAccount, SocialAccountId}
import domain.model.user.UserId
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
