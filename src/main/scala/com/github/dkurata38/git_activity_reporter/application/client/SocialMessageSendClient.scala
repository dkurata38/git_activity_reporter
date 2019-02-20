package com.github.dkurata38.git_activity_reporter.application.client

import com.github.dkurata38.git_activity_reporter.domain.social_account.SocialAccount
import com.github.dkurata38.git_activity_reporter.domain.social_message.SocialMessage

trait SocialMessageSendClient {
  def send(socialAccount: SocialAccount, socialMessage: SocialMessage): Unit
}
