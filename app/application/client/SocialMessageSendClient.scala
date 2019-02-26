package application.client

import domain.model.social.{SocialAccount, SocialMessage}

trait SocialMessageSendClient {
  def send(socialAccount: SocialAccount, socialMessage: SocialMessage): Unit
}
