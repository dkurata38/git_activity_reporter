package com.github.dkurata38.git_activity_reporter.application.service

import com.github.dkurata38.git_activity_reporter.application.repository.ISocialAccountRepository
import com.github.dkurata38.git_activity_reporter.domain.model.user.UserId
import com.github.dkurata38.git_activity_reporter.infrastracture.repository.social_account.SocialAccountRepository

class SocialAccountService(private val repository: ISocialAccountRepository = new SocialAccountRepository) {
  def getAllByUserId(userId: UserId) = repository.findAllByUserId(userId)
}
