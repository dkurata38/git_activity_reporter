package domain.user

import domain.{Enum, EnumValue}

sealed abstract class RegistrationStatus(val value: Int) extends EnumValue {
  override type Value = Int
}

object RegistrationStatus extends Enum[RegistrationStatus] {
  case object Temporary extends RegistrationStatus(0)
  case object Regular extends RegistrationStatus(1)

  override val values: Seq[RegistrationStatus] = Seq(Temporary, Regular)
}