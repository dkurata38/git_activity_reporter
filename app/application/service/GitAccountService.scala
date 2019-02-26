package application.service

import application.repository.IGitAccountRepository
import domain.model.user.UserId
import javax.inject.{Inject, Singleton}

@Singleton
class GitAccountService @Inject()(private val repository: IGitAccountRepository) {
  def oauthLogin() = ???

  def add() = ???

  def getAllByUserId(userId: UserId) = repository.findAllByUserId(userId)
}
