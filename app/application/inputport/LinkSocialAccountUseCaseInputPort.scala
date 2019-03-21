package application.inputport

import domain.model.social.{SocialAccount, SocialClientId}

trait LinkSocialAccountUseCaseInputPort {
  def link(token: String, clientId: SocialClientId, accessToken: String, accessTokenSecret: String): Either[String, SocialAccount]
}
