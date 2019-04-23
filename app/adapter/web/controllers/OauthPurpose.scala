package adapter.web.controllers

sealed trait OauthPurpose

object OauthPurpose{
  case object SignIn extends OauthPurpose
  case object SingUp extends OauthPurpose
  case object Link extends OauthPurpose
}
