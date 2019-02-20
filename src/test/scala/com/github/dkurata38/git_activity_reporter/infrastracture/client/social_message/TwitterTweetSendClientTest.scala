package com.github.dkurata38.git_activity_reporter.infrastracture.client.social_message

import com.github.dkurata38.git_activity_reporter.domain.social_account.SocialAccount
import com.github.dkurata38.git_activity_reporter.domain.social_account.SocialAccount.Twitter
import com.github.dkurata38.git_activity_reporter.domain.social_message.SocialMessage
import com.typesafe.config.ConfigFactory

object TwitterTweetSendClientTest extends App {
  val config = ConfigFactory.load()
  val client = new TwitterTweetSendClient
  val message = new SocialMessage("テストです")
  val account = new SocialAccount(1111, Twitter, config.getString("app.twitter.access_token"), config.getString("app.twitter.access_token_secret"))

  client.send(account, message)
}
