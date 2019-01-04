package com.github.dkurata38.git_activity_reporter.usecase.git_activity_summary

import com.github.dkurata38.git_activity_reporter.domain.git_account.GitAccount.ClientId.GitHub
import com.github.dkurata38.git_activity_reporter.infrastracture.client.git_event.github.GitHubEventClient
import com.github.dkurata38.git_activity_reporter.infrastracture.repository.git_account.GitAccountRepository

class GitActivitySummaryUseCase {
  def summarize(userId: Int): GitActivitySummaries = {
    val gitAccountRepository = new GitAccountRepository
    val gitAccount = gitAccountRepository.findAllByUserId(userId)

    val groupByRepo = gitAccount.flatMap{a => a.clientId match {
      case GitHub => new GitHubEventClient().getUserEvents(a)
    }}.groupBy(e => e.gitRepository)

    val gitActivitySummaries = new GitActivitySummaries()
    groupByRepo.foreach{es =>
      val groupByRepoAndEventType = es._2.groupBy(e => e.eventType)
      groupByRepoAndEventType.foreach(g => gitActivitySummaries.+:(new GitActivitySummary(es._1, g._1, g._2.size)))
    }
    gitActivitySummaries
  }
}
