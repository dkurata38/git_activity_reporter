package domain.git_activity

import domain.git_activity.GitActivityType.Push

class PushActivities(private val values: Seq[PushActivity]) {
  def countByRepositoryAndEventType() = {
    new PushActivitySummaries(values.groupBy(e => e.gitRepository).map(e => new PushActivitySummary(e._1, Push, e._2.size)).toSeq)
  }

  def ++(pushActivities: PushActivities) = new PushActivities(values ++ pushActivities.values)

  def +(pushActivity: PushActivity) = new PushActivities(values.+:(pushActivity))

  def length: Int = values.length

  def apply(idx: Int): PushActivity = values.apply(idx)

  def iterator: Iterator[PushActivity] = values.iterator

  def foreach(f: PushActivity => Unit) = values.foreach(f)

  def map[T](f: PushActivity => T) = values.map(f)
}

object PushActivities {
  def empty() = new PushActivities(Nil)
}
