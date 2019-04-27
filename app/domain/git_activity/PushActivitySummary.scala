package domain.git_activity


class PushActivitySummary(val source: Seq[PushActivity]) {
  def pushCount = source.size

  def commitsCount = source.map(value => value.commitCount).sum

  def commitsCountPerPush = commitsCount / pushCount
}