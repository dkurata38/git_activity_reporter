package application.service

import application.client.GitEventClientFactory
import application.repository.IGitAccountRepository
import domain.model.git.account.{AccessToken, GitAccount, GitClientId}
import domain.model.user.UserId
import javax.inject.{Inject, Singleton}

@Singleton
class GitAccountService @Inject()(private val repository: IGitAccountRepository) {
  def oauthLogin() = ???

  def add() = ???

  def link(gitAccount: GitAccount) = repository.create(gitAccount)

  def getAllByUserId(userId: UserId) = repository.findAllByUserId(userId)

  def getAuthenticatedUser(clientId: GitClientId, accessToken: AccessToken) = {
    new GitEventClientFactory().getInstance(clientId).getAuthenticatedUser(accessToken)
  }

  def getByClientIdAndUserName(clientId: GitClientId, userName: String) = repository.findByClientIdAndUserName(clientId, userName)
}
