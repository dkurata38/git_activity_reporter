package application.coordinator

import application.client.GitEventClientFactory
import application.service.GitAccountService
import domain.model.git.{GitActivitySummaries, GitEvents}
import domain.model.user.UserId
import javax.inject.{Inject, Singleton}

@Singleton
class GitActivitySummaryCoordinator @Inject()(private val accountService: GitAccountService) {
  def summarize(userId: UserId): GitActivitySummaries = {
    val gitAccounts = accountService.getAllByUserId(userId)

    val gitEvents = gitAccounts.map { a =>
      new GitEventClientFactory().getInstance(a.clientId).getUserEvents(a)
    }.foldRight(GitEvents.emptyCollection())((elem: GitEvents, accum: GitEvents) => accum ++ elem)

    gitEvents.countByRepositoryAndEventType()
  }
}
