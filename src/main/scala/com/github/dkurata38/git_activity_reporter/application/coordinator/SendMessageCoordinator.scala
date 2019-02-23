package com.github.dkurata38.git_activity_reporter.application.coordinator

import com.github.dkurata38.git_activity_reporter.application.client.SocialMessageClientFactory
import com.github.dkurata38.git_activity_reporter.application.service.SocialAccountService
import com.github.dkurata38.git_activity_reporter.domain.model.social.SocialMessage
import com.github.dkurata38.git_activity_reporter.domain.model.user.UserId

class SendMessageCoordinator(private val accountService: SocialAccountService = new SocialAccountService()) {
  def sendMessage(userId: UserId, message: SocialMessage) = {
    val accounts = accountService.getAllByUserId(userId)

    accounts.foreach(e => {
      new SocialMessageClientFactory().getInstance(e.clientId).send(e, message)
    })
  }
}
