package domain.`type`

sealed class SocialClientId(val value: String)

object SocialClientId {
  case object Twitter extends SocialClientId("twitter")
  def getByValue(value: String) = value match {
    case Twitter.`value` => Twitter
  }
}
