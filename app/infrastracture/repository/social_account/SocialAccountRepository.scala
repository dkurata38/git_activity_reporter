package infrastracture.repository.social_account

import application.repository.ISocialAccountRepository
import domain.model.social.{SocialAccessToken, SocialAccount, SocialAccountId, SocialClientId}
import domain.model.user.UserId
import javax.inject.{Inject, Singleton}
import scalikejdbc._

@Singleton
class SocialAccountRepository @Inject() extends ISocialAccountRepository {
  override def findAllByUserId(userId: UserId): Seq[SocialAccount] = {
    DB readOnly{ implicit session: DBSession =>
      sql"SELECT * FROM social_account WHERE user_account_id = ${userId.value}"
        .map(rs => new SocialAccount(
          UserId(rs.string("user_account_id")),
          SocialClientId.getByValue(rs.string("client_id")),
          SocialAccountId(rs.get("user_name")),
          SocialAccessToken(rs.string("access_token"), rs.string("access_token_secret"))
        )).list().apply()
    }
  }

  override def findOneByUserIdAndSocialClientId(userId: UserId, clientId: SocialClientId): Option[SocialAccount] = {
    DB readOnly{ implicit session: DBSession =>
      sql"SELECT * FROM social_account WHERE user_account_id = ${userId.value} AND client_id = ${clientId.value}"
        .map(rs => socialAccountMap(rs)).first().apply()
    }
  }

  override def findOneBySocialAccountId(clientId: SocialClientId, accountId: SocialAccountId): Option[SocialAccount] = {
    DB readOnly{ implicit session: DBSession =>
      sql"SELECT * FROM social_account WHERE user_name = ${accountId.value} AND client_id = ${clientId.value}"
        .map(rs => socialAccountMap(rs)).first().apply()
    }
  }

  override def create(socialAccount: SocialAccount): SocialAccount = ???

  def socialAccountMap(rs: WrappedResultSet) = {
    new SocialAccount(
      UserId(rs.string("user_account_id")),
      SocialClientId.getByValue(rs.string("client_id")),
      SocialAccountId(rs.get("user_name")),
      SocialAccessToken(rs.string("access_token"), rs.string("access_token_secret"))
    )
  }
}
