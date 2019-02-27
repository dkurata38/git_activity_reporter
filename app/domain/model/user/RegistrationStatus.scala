package domain.model.user

sealed abstract class RegistrationStatus(val value: Int)

object RegistrationStatus {
  case object Temporary extends RegistrationStatus(0)
  case object Regular extends RegistrationStatus(1)

  def getByValue(value: Int): RegistrationStatus = value match {
    case Temporary.value => Temporary
    case Regular.value => Regular
    case _ => throw new IllegalArgumentException
  }
}
