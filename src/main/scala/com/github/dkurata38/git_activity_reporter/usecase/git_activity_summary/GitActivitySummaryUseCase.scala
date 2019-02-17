package com.github.dkurata38.git_activity_reporter.usecase.git_activity_summary

import com.github.dkurata38.git_activity_reporter.domain.git_event.GitEvent.Push
import com.github.dkurata38.git_activity_reporter.domain.git_event.GitEventClientFactory
import com.github.dkurata38.git_activity_reporter.infrastracture.repository.git_account.GitAccountRepository

class GitActivitySummaryUseCase {
  def summarize(userId: Int): GitActivitySummaries = {
    val gitAccountRepository = new GitAccountRepository
    val gitAccount = gitAccountRepository.findAllByUserId(userId)

    val groupByRepo = gitAccount.flatMap{a =>
      new GitEventClientFactory().getInstance(a.clientId).getUserEvents(a)
    }.groupBy(e => e.gitRepository)

    val gitActivitySummaries = groupByRepo
      .map(es => new GitActivitySummary(es._1, Push, es._2.size))
      .toSeq

    new GitActivitySummaries(gitActivitySummaries)
  }
}
