package infrastracture.client.social_message

import application.client.SocialMessageSendClient
import domain.model.social.{SocialAccount, SocialMessage}
import javax.inject.Singleton
import twitter4j.TwitterFactory
import twitter4j.auth.AccessToken

@Singleton
class TwitterTweetSendClient extends SocialMessageSendClient {
  override def send(socialAccount: SocialAccount, socialMessage: SocialMessage): Unit = {
    val accessToken = new AccessToken(socialAccount.accessToken.token, socialAccount.accessToken.secret)
    val twitter = new TwitterFactory().getInstance(accessToken)
    twitter.updateStatus(socialMessage.message)
  }
}
