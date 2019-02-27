package com.github.dkurata38.git_activity_reporter.application.coordinator

import com.github.dkurata38.git_activity_reporter.application.client.GitEventClientFactory
import com.github.dkurata38.git_activity_reporter.application.service.GitAccountService
import com.github.dkurata38.git_activity_reporter.domain.model.git.{GitActivitySummaries, GitEvents}
import com.github.dkurata38.git_activity_reporter.domain.model.user.UserId
import javax.inject.{Inject, Singleton}

@Singleton
class GitEventsCoordinator @Inject()(private val accountService: GitAccountService){
  def summarize(userId: UserId): GitActivitySummaries = {
    val gitEvents = getGitEvents(userId)
    gitEvents.countByRepositoryAndEventType()
  }

  def getGitEvents(userId: UserId): GitEvents = {
    val gitAccounts = accountService.getAllByUserId(userId)

    gitAccounts.map { a =>
      new GitEventClientFactory().getInstance(a.clientId).getUserEvents(a)
    }.foldRight(GitEvents.emptyCollection())((elem: GitEvents, accum: GitEvents) => accum ++ elem)
  }
}
