package com.github.dkurata38.git_activity_reporter.infrastracture.repository.git_account

import com.github.dkurata38.git_activity_reporter.domain.git_account.{GitAccount, IGitAccountRepository}

class GitAccountRepository extends IGitAccountRepository{
  override def findAllByUserId(userId: Int): Seq[GitAccount] = ???

  override def findOneByUserIdAndGitProviderId(userId: Int): Option[GitAccount] = ???
}
