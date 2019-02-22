package com.github.dkurata38.git_activity_reporter.domain.model.git

import com.github.dkurata38.git_activity_reporter.domain.`type`.GitEventType.Push

class GitEvents (private val events: Seq[GitEvent]) extends Seq[GitEvent]{
  def countByRepositoryAndEventType() = {

    val gitActivitySummaries = events.groupBy(e => e.gitRepository).map(e => new GitActivitySummary(e._1, Push, e._2.size)).toList
    new GitActivitySummaries(gitActivitySummaries)
  }

  def ++ (gitEvents: GitEvents) = {
    new GitEvents(events ++ gitEvents.events)
  }

  override def length: Int = events.length

  override def apply(idx: Int): GitEvent = events.apply(idx)

  override def iterator: Iterator[GitEvent] = events.iterator
}

object GitEvents {
  def emptyCollection() = new GitEvents(Nil)
}
