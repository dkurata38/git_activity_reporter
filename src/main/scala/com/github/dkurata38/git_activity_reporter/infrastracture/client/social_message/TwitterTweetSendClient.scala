package com.github.dkurata38.git_activity_reporter.infrastracture.client.social_message

import com.github.dkurata38.git_activity_reporter.application.client.SocialMessageSendClient
import com.github.dkurata38.git_activity_reporter.domain.social_account.SocialAccount
import com.github.dkurata38.git_activity_reporter.domain.social_message.SocialMessage
import twitter4j.TwitterFactory
import twitter4j.auth.AccessToken

class TwitterTweetSendClient extends SocialMessageSendClient{
  override def send(socialAccount: SocialAccount, socialMessage: SocialMessage): Unit = {
    val accessToken = new AccessToken(socialAccount.accessToken, socialAccount.accessTokenSecret)
    val twitter = new TwitterFactory().getInstance(accessToken)
    twitter.updateStatus(socialMessage.message)
  }
}
