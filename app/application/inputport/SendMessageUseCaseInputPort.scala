package application.inputport

import domain.social.SocialClientId

trait SendMessageUseCaseInputPort {
  def sendMessage(token: String, clientId: SocialClientId, message: String)
}
