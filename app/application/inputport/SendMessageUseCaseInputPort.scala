package application.inputport

import domain.model.social.SocialClientId

trait SendMessageUseCaseInputPort {
  def sendMessage(token: String, clientId: SocialClientId, message: String)
}
