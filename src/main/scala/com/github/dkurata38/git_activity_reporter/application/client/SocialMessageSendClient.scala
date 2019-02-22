package com.github.dkurata38.git_activity_reporter.application.client

import com.github.dkurata38.git_activity_reporter.domain.model.social.{SocialAccount, SocialMessage}

trait SocialMessageSendClient {
  def send(socialAccount: SocialAccount, socialMessage: SocialMessage): Unit
}
