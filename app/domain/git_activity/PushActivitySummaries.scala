package domain.git_activity

import scala.collection.generic.CanBuildFrom

class PushActivitySummaries(val gitActivitySummaries: Seq[PushActivitySummary] = Nil) {
  def head: PushActivitySummary = gitActivitySummaries.head

  def +:[B >: PushActivitySummary, That](elem: B)(implicit bf: CanBuildFrom[Seq[PushActivitySummary], B, That]): That = gitActivitySummaries.+:(elem)(bf)

  def foreach(consumer: PushActivitySummary => Unit) = gitActivitySummaries.foreach(consumer)

  def map[T](f: PushActivitySummary => T) = gitActivitySummaries.map(f)
}
