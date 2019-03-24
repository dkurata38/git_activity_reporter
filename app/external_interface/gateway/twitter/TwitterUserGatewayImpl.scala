package external_interface.gateway.twitter

import adapter.gateway.twitter.TwitterUserGateway
import domain.social.SocialClientId.Twitter
import domain.social.{SocialAccessToken, SocialAccount, SocialAccountId}
import javax.inject.{Inject, Singleton}
import twitter4j.TwitterFactory
import twitter4j.auth.AccessToken

@Singleton
class TwitterUserGatewayImpl @Inject()() extends TwitterUserGateway {
  override def getUser(accessToken: String, accessTokenSecret: String): Option[SocialAccount] = {
    val twitter = new TwitterFactory().getInstance(new AccessToken(accessToken, accessTokenSecret))
    Option(twitter.users().verifyCredentials()).map(user =>
      new SocialAccount(null, Twitter, SocialAccountId(user.getScreenName), SocialAccessToken(accessToken, accessTokenSecret))
    )
  }
}
