package external_interface.gateway.twitter

import adapter.gateway.twitter.TwitterMessageSendGateway
import domain.social.{SocialAccount, SocialMessage}
import javax.inject.Singleton
import twitter4j.TwitterFactory
import twitter4j.auth.AccessToken

@Singleton
class TwitterMessageSendGatewayImpl extends TwitterMessageSendGateway {
  override def send(socialAccount: SocialAccount, socialMessage: SocialMessage): Unit = {
    val accessToken = new AccessToken(socialAccount.accessToken.token, socialAccount.accessToken.secret)
    val twitter = new TwitterFactory().getInstance(accessToken)
    twitter.updateStatus(socialMessage.message)
  }
}
