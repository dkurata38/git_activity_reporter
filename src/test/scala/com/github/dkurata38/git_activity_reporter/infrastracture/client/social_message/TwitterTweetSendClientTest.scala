package com.github.dkurata38.git_activity_reporter.infrastracture.client.social_message

import com.github.dkurata38.git_activity_reporter.domain.`type`.SocialClientId.Twitter
import com.github.dkurata38.git_activity_reporter.domain.model.social.{SocialAccount, SocialMessage}
import com.github.dkurata38.git_activity_reporter.domain.model.user.UserId
import com.typesafe.config.ConfigFactory

object TwitterTweetSendClientTest extends App {
  val config = ConfigFactory.load()
  val client = new TwitterTweetSendClient
  val message = new SocialMessage("テストです")
  val account = new SocialAccount(UserId(1111), Twitter, config.getString("app.twitter.access_token"), config.getString("app.twitter.access_token_secret"))

  client.send(account, message)
}
