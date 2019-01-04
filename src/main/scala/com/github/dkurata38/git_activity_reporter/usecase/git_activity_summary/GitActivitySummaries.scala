package com.github.dkurata38.git_activity_reporter.usecase.git_activity_summary

import scala.collection.generic.CanBuildFrom

class GitActivitySummaries (val gitActivitySummaries: Seq[GitActivitySummary] = Nil) {
  def head: GitActivitySummary = gitActivitySummaries.head

  def +:[B >: GitActivitySummary, That](elem: B)(implicit bf: CanBuildFrom[Seq[GitActivitySummary], B, That]): That = gitActivitySummaries.+:(elem)(bf)
}
