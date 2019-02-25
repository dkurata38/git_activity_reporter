package com.github.dkurata38.git_activity_reporter.application.service

import com.github.dkurata38.git_activity_reporter.application.repository.IUserRepository
import com.github.dkurata38.git_activity_reporter.domain.model.user.User
import javax.inject.{Inject, Singleton}

@Singleton
class UserService @Inject() (private val repository: IUserRepository) {
  def registerUser(user: User): User = repository.create(user)
}
