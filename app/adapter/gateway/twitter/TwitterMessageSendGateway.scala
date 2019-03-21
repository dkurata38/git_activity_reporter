package adapter.gateway.twitter

import domain.social.{SocialAccount, SocialMessage}

trait TwitterMessageSendGateway {
  def send(socialAccount: SocialAccount, socialMessage: SocialMessage): Unit
}
