package com.github.dkurata38.git_activity_reporter.client.git_event.github

import com.github.dkurata38.git_activity_reporter.domain.git_account.GitAccount
import com.github.dkurata38.git_activity_reporter.domain.git_account.GitAccount.ClientId.GitHub
import com.github.dkurata38.git_activity_reporter.infrastracture.client.git_event.github.GitHubEventClient
import com.typesafe.config.ConfigFactory

object GitHubEventClientTest extends App {
  val config = ConfigFactory.load()
  val gitHubEventClient = new GitHubEventClient()
  val gitHubAccount = new GitAccount(1111, GitHub, config.getString("app.github.user_name"), config.getString("app.github.access_token"))
  val events = gitHubEventClient.getUserEvents(gitHubAccount)
  events.foreach(f => println(s"リポジトリ:${f.gitRepository}, イベントタイプ:${f.eventType}"))
}
