package application.repository

import domain.`type`.GitClientId
import domain.model.git.GitAccount
import domain.model.user.UserId

trait IGitAccountRepository {
  def findAllByUserId(userId: UserId): Seq[GitAccount]

  def findOneByUserIdAndClientId(userId: UserId, clientId: GitClientId): Option[GitAccount]

  def create(gitAccount: GitAccount)
}
