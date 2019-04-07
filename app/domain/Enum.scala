package domain

trait EnumValue {
  type Value
  val value: Value
}

trait Enum[A <: EnumValue]{
  val values: Seq[A]
  def apply(value: A#Value): A = values.find(_.value == value).get
}
