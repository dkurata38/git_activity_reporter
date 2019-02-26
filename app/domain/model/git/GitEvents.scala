package domain.model.git

import domain.`type`.GitEventType.Push

class GitEvents(private val events: Seq[GitEvent]) {
  def countByRepositoryAndEventType() = {

    val gitActivitySummaries = events.groupBy(e => e.gitRepository).map(e => new GitActivitySummary(e._1, Push, e._2.size)).toSeq
    new GitActivitySummaries(gitActivitySummaries)
  }

  def ++(gitEvents: GitEvents) = {
    new GitEvents(events ++ gitEvents.events)
  }

  def length: Int = events.length

  def apply(idx: Int): GitEvent = events.apply(idx)

  def iterator: Iterator[GitEvent] = events.iterator

  def foreach(f: GitEvent => Unit) = events.foreach(f)

  def map[T](f: GitEvent => T) = events.map(f)
}

object GitEvents {
  def emptyCollection() = new GitEvents(Nil)
}
