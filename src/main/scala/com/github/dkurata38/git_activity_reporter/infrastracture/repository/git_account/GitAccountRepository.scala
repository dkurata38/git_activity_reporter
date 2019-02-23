package com.github.dkurata38.git_activity_reporter.infrastracture.repository.git_account

import com.github.dkurata38.git_activity_reporter.application.repository.IGitAccountRepository
import com.github.dkurata38.git_activity_reporter.domain.`type`.GitClientId
import com.github.dkurata38.git_activity_reporter.domain.`type`.GitClientId.GitHub
import com.github.dkurata38.git_activity_reporter.domain.model.git.GitAccount
import com.github.dkurata38.git_activity_reporter.domain.model.user.UserId
import com.typesafe.config.ConfigFactory

class GitAccountRepository extends IGitAccountRepository{
  val config = ConfigFactory.load()

  override def findAllByUserId(userId: UserId): Seq[GitAccount] = {
    val githubAccount = new GitAccount(UserId(1111), GitHub, config.getString("app.github.user_name"), config.getString("app.github.access_token"))
    Seq(githubAccount)
  }

  override def findOneByUserIdAndGitProviderId(userId: UserId, clientId: GitClientId): Option[GitAccount] = ???
}
