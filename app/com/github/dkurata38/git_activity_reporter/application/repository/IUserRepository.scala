package com.github.dkurata38.git_activity_reporter.application.repository

import com.github.dkurata38.git_activity_reporter.domain.model.user.User

trait IUserRepository {
  def create(user: User): User
}
