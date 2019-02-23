package com.github.dkurata38.git_activity_reporter.application.repository

import com.github.dkurata38.git_activity_reporter.domain.`type`.GitClientId
import com.github.dkurata38.git_activity_reporter.domain.model.git.GitAccount
import com.github.dkurata38.git_activity_reporter.domain.model.user.UserId

trait IGitAccountRepository {
  def findAllByUserId(userId: UserId): Seq[GitAccount]

  def findOneByUserIdAndGitProviderId(userId: UserId, clientId: GitClientId): Option[GitAccount]
}
