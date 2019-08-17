package domain.git.activity

class PushActivitySummary(val source: Seq[PushActivity]) {
  def pushCount: Int = source.size

  def commitsCount: Int = source.map(value => value.commitCount).sum

  def commitsCountPerPush: Int = if (pushCount == 0) commitsCount else pushCount
}
