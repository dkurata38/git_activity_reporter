package domain.social

import domain.{Enum, EnumValue}

sealed abstract class SocialClientId(val value: String) extends EnumValue {
  override type Value = String
}

object SocialClientId extends Enum[SocialClientId]{
  case object Twitter extends SocialClientId("twitter")
  override val values: Seq[SocialClientId] = Seq(Twitter)
}
