package application.repository

import domain.model.user.{RegistrationStatus, User, UserId, UserRepository}
import javax.inject.{Inject, Singleton}
import scalikejdbc.{DB, DBSession, _}

@Singleton
class UserRepositoryImpl @Inject() extends UserRepository {
  override def create(user: User) = {
    DB autoCommit {implicit session: DBSession =>
      sql"INSERT INTO user_account(id, registration_status) VALUES (${user.userId.value}, ${user.registrationStatus.value})"
        .update().apply()
    }
  }

  override def findOneById(userId: UserId): Option[User] = DB readOnly { implicit session: DBSession =>
    sql"SELECT * FROM user_account WHERE id = ${userId.value}"
      .map(rs => userAccountMap(rs)).first().apply()
  }

  def userAccountMap(rs: WrappedResultSet) = new User(
    UserId(rs.string("id")),
    RegistrationStatus.getByValue(rs.int("registration_status"))
  )

  override def update(user: User): Int = DB autoCommit { implicit session: DBSession =>
    sql"UPDATE user_account SET registration_status = ${user.registrationStatus.value} WHERE id = ${user.userId.value}"
      .update().apply()
  }
}
