package com.github.dkurata38.git_activity_reporter.application.client

import com.github.dkurata38.git_activity_reporter.domain.`type`.SocialClientId
import com.github.dkurata38.git_activity_reporter.domain.`type`.SocialClientId.Twitter
import com.github.dkurata38.git_activity_reporter.infrastracture.client.social_message.TwitterTweetSendClient

class SocialMessageClientFactory {
  def getInstance(clientId: SocialClientId) :SocialMessageSendClient = {
    clientId match {
      case Twitter => new TwitterTweetSendClient
    }
  }
}
