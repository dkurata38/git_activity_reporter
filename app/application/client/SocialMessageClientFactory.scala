package application.client

import domain.`type`.SocialClientId
import domain.`type`.SocialClientId.Twitter
import infrastracture.client.social_message.TwitterTweetSendClient

class SocialMessageClientFactory {
  def getInstance(clientId: SocialClientId): SocialMessageSendClient = {
    clientId match {
      case Twitter => new TwitterTweetSendClient
    }
  }
}
