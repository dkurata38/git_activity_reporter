package application.repository

import domain.user.UserId
import domain.user_token.{Token, UserToken, UserTokenRepository}
import javax.inject.{Inject, Singleton}
import scalikejdbc.{DB, DBSession, _}

@Singleton
class UserTokenRepositoryImpl @Inject() extends UserTokenRepository{
  override def findByUserId(userId: UserId): Option[UserToken] =
    DB readOnly {implicit session: DBSession =>
      sql"SELECT user_account_id, token FROM user_token WHERE user_account_id = ${userId.value}"
        .map(rs => rs.toUserToken).first().apply()
    }


  override def findByUserToken(token: Token): Option[UserToken] =
    DB readOnly { implicit session: DBSession =>
      sql"SELECT user_account_id, token FROM user_token WHERE token = ${token.value}"
        .map(rs => rs.toUserToken).first().apply()
    }

  override def deleteByToken(token: Token): Int =
    DB autoCommit {implicit session: DBSession =>
      sql"DELETE FROM user_token WHERE token = ${token.value}"
        .update().apply()

    }

  override def create(userToken: UserToken): Int = {
    DB autoCommit {implicit session: DBSession =>
      sql"INSERT INTO user_token(user_account_id, token) VALUES (${userToken.userId.value}, ${userToken.token.value})"
        .update().apply()
    }
  }

  implicit class UserTokenMap(rs: WrappedResultSet) {
    def toUserToken = UserToken(
      UserId(rs.string("user_account_id"))
        ,Token(rs.string("token"))
    )
  }
}
