package domain.model.git.activity

class GitActivities(private val values: Seq[GitActivity]) {
  def countByRepositoryAndEventType() = {

    val gitActivitySummaries = values.groupBy(e => e.gitRepository).map(e => new GitActivitySummary(e._1, Push, e._2.size)).toSeq
    new GitActivitySummaries(gitActivitySummaries)
  }

  def ++(gitEvents: GitActivities) = new GitActivities(values ++ gitEvents.values)

  def +(gitActivity: GitActivity) = new GitActivities(values.+:(gitActivity))

  def length: Int = values.length

  def apply(idx: Int): GitActivity = values.apply(idx)

  def iterator: Iterator[GitActivity] = values.iterator

  def foreach(f: GitActivity => Unit) = values.foreach(f)

  def map[T](f: GitActivity => T) = values.map(f)
}

object GitActivities {
  def empty() = new GitActivities(Nil)
}
