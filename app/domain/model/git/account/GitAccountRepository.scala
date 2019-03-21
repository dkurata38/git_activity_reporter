package domain.model.git.account

import domain.model.user.UserId

trait GitAccountRepository {
  def getUserFromClient(clientId: GitClientId, accessToken: AccessToken): GitAccount

  def findAllByUserId(userId: UserId): Seq[GitAccount]

  def findOneByUserIdAndClientId(userId: UserId, clientId: GitClientId): Option[GitAccount]

  def create(gitAccount: GitAccount)

  def findByClientIdAndUserName(clientId: GitClientId, userName: String) : Option[GitAccount]
}
