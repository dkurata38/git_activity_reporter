package domain.git.activity

case class Count(value: Int) {
  def + (that: Count) = Count(this.value + that.value)

  def / (divisor: Count) = Count(if (divisor.value == 0) 0 else this.value / divisor.value)
}
