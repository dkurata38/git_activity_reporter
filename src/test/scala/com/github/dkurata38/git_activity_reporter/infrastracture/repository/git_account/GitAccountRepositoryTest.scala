package com.github.dkurata38.git_activity_reporter.infrastracture.repository.git_account

import com.github.dkurata38.git_activity_reporter.domain.model.user.UserId

object GitAccountRepositoryTest extends App {
  val repository = new GitAccountRepository
  val accounts = repository.findAllByUserId(UserId(1111))
  accounts.foreach(e => println(s"${e.gitUserName}"))
}
