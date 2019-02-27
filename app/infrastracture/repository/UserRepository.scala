package infrastracture.repository

import application.repository.IUserRepository
import domain.model.user.User
import javax.inject.{Inject, Singleton}
import scalikejdbc.{DB, DBSession, _}

@Singleton
class UserRepository @Inject() extends IUserRepository {
  override def create(user: User) = {
    DB autoCommit {implicit session: DBSession =>
      sql"INSERT INTO user_account(id) VALUES (${user.userId.value})"
        .update().apply()
    }
  }
}
