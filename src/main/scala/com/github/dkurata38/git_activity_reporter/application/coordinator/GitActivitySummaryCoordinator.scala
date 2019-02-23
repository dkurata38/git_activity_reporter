package com.github.dkurata38.git_activity_reporter.application.coordinator

import com.github.dkurata38.git_activity_reporter.application.client.GitEventClientFactory
import com.github.dkurata38.git_activity_reporter.application.service.GitAccountService
import com.github.dkurata38.git_activity_reporter.domain.model.git.{GitActivitySummaries, GitEvents}
import com.github.dkurata38.git_activity_reporter.domain.model.user.UserId

class GitActivitySummaryCoordinator(private val accountService: GitAccountService = new GitAccountService()){
  def summarize(userId: UserId): GitActivitySummaries = {
    val gitAccounts = accountService.getAllByUserId(userId)

    val gitEvents = gitAccounts.map { a =>
      new GitEventClientFactory().getInstance(a.clientId).getUserEvents(a)
    }.foldRight(GitEvents.emptyCollection())((elem: GitEvents, accum: GitEvents) => accum ++ elem)

    gitEvents.countByRepositoryAndEventType()
  }
}
