package com.github.dkurata38.git_activity_reporter.infrastracture.client.social_message

import com.github.dkurata38.git_activity_reporter.application.client.SocialMessageSendClient
import com.github.dkurata38.git_activity_reporter.domain.model.social.{SocialAccount, SocialMessage}
import javax.inject.Singleton
import twitter4j.TwitterFactory
import twitter4j.auth.AccessToken

@Singleton
class TwitterTweetSendClient extends SocialMessageSendClient{
  override def send(socialAccount: SocialAccount, socialMessage: SocialMessage): Unit = {
    val accessToken = new AccessToken(socialAccount.accessToken.token, socialAccount.accessToken.secret)
    val twitter = new TwitterFactory().getInstance(accessToken)
    twitter.updateStatus(socialMessage.message)
  }
}
