package com.github.dkurata38.git_activity_reporter.application.usecase.git_activity_summary

import com.github.dkurata38.git_activity_reporter.application.client.GitEventClientFactory
import com.github.dkurata38.git_activity_reporter.domain.model.git.{GitActivitySummaries, GitEvents}
import com.github.dkurata38.git_activity_reporter.infrastracture.repository.git_account.GitAccountRepository

class GitActivitySummaryUseCase {
  def summarize(userId: Int): GitActivitySummaries = {
    val gitAccountRepository = new GitAccountRepository
    val gitAccount = gitAccountRepository.findAllByUserId(userId)

    val gitEvents = gitAccount.map { a =>
      new GitEventClientFactory().getInstance(a.clientId).getUserEvents(a)
    }.foldRight(GitEvents.emptyCollection())((elem: GitEvents, accum: GitEvents) => accum ++ elem)

    gitEvents.countByRepositoryAndEventType()
  }
}
