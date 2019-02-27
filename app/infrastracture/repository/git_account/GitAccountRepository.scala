package infrastracture.repository.git_account

import application.repository.IGitAccountRepository
import domain.model.git.account.{AccessToken, GitAccount, GitClientId}
import domain.model.user.UserId
import javax.inject.{Inject, Singleton}
import scalikejdbc._

@Singleton
class GitAccountRepository @Inject() extends IGitAccountRepository {
  override def findAllByUserId(userId: UserId): Seq[GitAccount] = {
    DB readOnly { implicit session: DBSession =>
      sql"SELECT * FROM git_account WHERE user_account_id = ${userId.value}"
        .map { rs => gitAccountMap(rs) }.list.apply()
    }
  }


  override def findOneByUserIdAndClientId(userId: UserId, clientId: GitClientId): Option[GitAccount] = {
    DB readOnly { implicit session: DBSession =>
      sql"SELECT * FROM git_account WHERE user_account_id = ${userId.value} AND client_id = ${clientId.value}"
        .map(rs => gitAccountMap(rs)).first().apply()
    }
  }

  override def create(gitAccount: GitAccount): Unit = {
    DB autoCommit {implicit session: DBSession =>
      sql"INSERT INTO git_account(user_account_id, client_id, user_name, access_token) VALUES (${gitAccount.userId.value}, ${gitAccount.clientId.value}, ${gitAccount.gitUserName}, ${gitAccount.accessToken.value})"
        .update().apply()
    }
  }

  def gitAccountMap(rs: WrappedResultSet) = new GitAccount(
    UserId(rs.string("user_id")),
    GitClientId.getByValue(rs.string("client_id")),
    rs.string("user_name"),
    AccessToken(rs.string("access_token"))
  )
}
