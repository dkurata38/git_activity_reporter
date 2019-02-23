package com.github.dkurata38.git_activity_reporter.application.repository

import com.github.dkurata38.git_activity_reporter.domain.`type`.GitClientId
import com.github.dkurata38.git_activity_reporter.domain.model.git.GitAccount

trait IGitAccountRepository {
  def findAllByUserId(userId: Int): Seq[GitAccount]

  def findOneByUserIdAndGitProviderId(userId: Int, clientId: GitClientId): Option[GitAccount]
}
