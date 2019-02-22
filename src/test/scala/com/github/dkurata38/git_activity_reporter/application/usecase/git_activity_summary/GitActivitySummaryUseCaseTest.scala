package com.github.dkurata38.git_activity_reporter.application.usecase.git_activity_summary

object GitActivitySummaryUseCaseTest extends App {
  val gitActivitySummaryUseCase = new GitActivitySummaryUseCase
  val gitActivitySummaries = gitActivitySummaryUseCase.summarize(1111)
  gitActivitySummaries.foreach(e => println(s"リポジトリ ${e.gitRepository.repositoryId.value} ${e.gitRepository.repositoryUrl} タイプ/数 ${e.eventType}/${e.count}"))
}
