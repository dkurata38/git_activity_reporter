package domain.git_activity

class PushActivities(private val values: Seq[PushActivity]) {
  def toSummary = {
    new PushActivitySummary(values)
  }

  def groupByRepository = {
    values.groupBy(e => e.gitRepository).map{case(gitRepository, pushActivities) => (gitRepository, new PushActivities(pushActivities))}
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
