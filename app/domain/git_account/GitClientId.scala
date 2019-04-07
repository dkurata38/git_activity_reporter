package domain.git_account

import domain.{Enum, EnumValue}

sealed abstract class GitClientId(val value: String) extends EnumValue {
  override type Value = String
}

object GitClientId extends Enum[GitClientId]{
  case object GitHub extends GitClientId("GitHub")
  case object GitLab extends GitClientId("GitLab")
  case object BitBucket extends GitClientId("BitBucket")

  override val values: Seq[GitClientId] = Seq(GitHub, GitLab, BitBucket)
}
