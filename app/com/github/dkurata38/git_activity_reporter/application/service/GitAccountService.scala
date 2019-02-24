package com.github.dkurata38.git_activity_reporter.application.service

import com.github.dkurata38.git_activity_reporter.application.repository.IGitAccountRepository
import com.github.dkurata38.git_activity_reporter.domain.model.user.UserId
import com.github.dkurata38.git_activity_reporter.infrastracture.repository.git_account.GitAccountRepository

class GitAccountService(private val repository: IGitAccountRepository = new GitAccountRepository) {
  def oauthLogin() = ???
  def add() = ???

  def getAllByUserId(userId: UserId) = repository.findAllByUserId(userId)
}
