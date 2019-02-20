package com.github.dkurata38.git_activity_reporter.application.repository

import com.github.dkurata38.git_activity_reporter.domain.git_account.GitAccount

trait IGitAccountRepository {
  def findAllByUserId(userId: Int): Seq[GitAccount]

  def findOneByUserIdAndGitProviderId(userId: Int): Option[GitAccount]
}
