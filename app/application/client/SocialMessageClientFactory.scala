package application.client

import domain.model.social.SocialClientId
import domain.model.social.SocialClientId.Twitter
import infrastracture.client.social_message.TwitterTweetSendClient

class SocialMessageClientFactory {
  def getInstance(clientId: SocialClientId): SocialMessageSendClient = {
    clientId match {
      case Twitter => new TwitterTweetSendClient
      case _ => throw new IllegalArgumentException
    }
  }
}
