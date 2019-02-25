package com.github.dkurata38.git_activity_reporter.application.service

import com.github.dkurata38.git_activity_reporter.application.repository.IGitAccountRepository
import com.github.dkurata38.git_activity_reporter.domain.model.user.UserId
import javax.inject.{Inject, Singleton}

@Singleton
class GitAccountService @Inject() (private val repository: IGitAccountRepository) {
  def oauthLogin() = ???
  def add() = ???

  def getAllByUserId(userId: UserId) = repository.findAllByUserId(userId)
}
