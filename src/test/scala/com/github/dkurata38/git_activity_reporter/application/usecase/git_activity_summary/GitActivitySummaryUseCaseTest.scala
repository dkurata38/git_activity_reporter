package com.github.dkurata38.git_activity_reporter.application.usecase.git_activity_summary

import com.github.dkurata38.git_activity_reporter.application.coordinator.GitActivitySummaryCoordinator
import com.github.dkurata38.git_activity_reporter.domain.model.user.UserId

object GitActivitySummaryUseCaseTest extends App {
  val gitActivitySummaryUseCase = new GitActivitySummaryCoordinator
  val gitActivitySummaries = gitActivitySummaryUseCase.summarize(UserId(1111))
  gitActivitySummaries.foreach(e => println(s"リポジトリ ${e.gitRepository.repositoryId.value} ${e.gitRepository.repositoryUrl} タイプ/数 ${e.eventType}/${e.count}"))
}
