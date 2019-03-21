package adapter.gateway.twitter

import domain.model.social.{SocialAccount, SocialMessage}

trait TwitterMessageSendGatewayImpl {
  def send(socialAccount: SocialAccount, socialMessage: SocialMessage): Unit
}
