package com.github.dkurata38.git_activity_reporter.infrastracture.repository.git_account

object GitAccountRepositoryTest extends App {
  val repository = new GitAccountRepository
  val accounts = repository.findAllByUserId(1111)
  accounts.foreach(e => println(s"${e.gitUserName}"))
}
