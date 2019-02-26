package application.coordinator

import application.client.SocialMessageClientFactory
import application.service.SocialAccountService
import domain.model.social.SocialMessage
import domain.model.user.UserId

class SendMessageCoordinator(private val accountService: SocialAccountService) {
  def sendMessage(userId: UserId, message: SocialMessage) = {
    val accounts = accountService.getAllByUserId(userId)

    accounts.foreach(e => {
      new SocialMessageClientFactory().getInstance(e.clientId).send(e, message)
    })
  }
}
