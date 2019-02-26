package domain.`type`

sealed class SocialClientId(val clientId: String)

object SocialClientId {

  case object Twitter extends SocialClientId("twitter")

}
