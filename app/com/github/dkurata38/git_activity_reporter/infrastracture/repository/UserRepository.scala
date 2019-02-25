package com.github.dkurata38.git_activity_reporter.infrastracture.repository

import com.github.dkurata38.git_activity_reporter.application.repository.IUserRepository
import com.github.dkurata38.git_activity_reporter.domain.model.user.User
import javax.inject.{Inject, Singleton}

@Singleton
class UserRepository @Inject() extends IUserRepository {
  override def create(user: User): User = ???
}
