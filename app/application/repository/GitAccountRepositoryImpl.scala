package application.repository

import adapter.gateway.github.GitHubUserGateway
import domain.model.git.account.GitClientId.GitHub
import domain.model.git.account.{AccessToken, GitAccount, GitAccountRepository, GitClientId}
import domain.model.user.UserId
import javax.inject.{Inject, Singleton}
import scalikejdbc.{DB, DBSession, _}

@Singleton
class GitAccountRepositoryImpl @Inject() (private implicit val gitHubUserGateway: GitHubUserGateway) extends GitAccountRepository {
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
    UserId(rs.string("user_account_id")),
    GitClientId.getByValue(rs.string("client_id")),
    rs.string("user_name"),
    AccessToken(rs.string("access_token"))
  )

  override def findByClientIdAndUserName(clientId: GitClientId, userName: String) = {
    DB readOnly { implicit session: DBSession =>
      sql"SELECT * FROM git_account WHERE user_name = ${userName} AND client_id = ${clientId.value}"
        .map(rs => gitAccountMap(rs)).first().apply()
    }
  }
  override def getUserFromClient(clientId: GitClientId, accessToken: AccessToken): GitAccount = clientId match {
    case GitHub => gitHubUserGateway.getUser(accessToken)
    case _ => ???
  }
}
