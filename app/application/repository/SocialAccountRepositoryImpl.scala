package application.repository

import adapter.gateway.twitter.TwitterUserGateway
import domain.model.social.SocialClientId.Twitter
import domain.model.social._
import domain.model.user.UserId
import javax.inject.{Inject, Singleton}
import scalikejdbc._

@Singleton
class SocialAccountRepositoryImpl @Inject() (private val twitterUserGateway: TwitterUserGateway) extends SocialAccountRepository {
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

  override def create(socialAccount: SocialAccount): Int = DB autoCommit { implicit session: DBSession =>
    sql"INSERT INTO social_account(user_account_id, client_id, user_name, access_token, access_token_secret) VALUES (${socialAccount.userId.value}, ${socialAccount.clientId.value}, ${socialAccount.socialUserId.value}, ${socialAccount.accessToken.token}, ${socialAccount.accessToken.secret})"
        .update().apply()
  }

  def socialAccountMap(rs: WrappedResultSet) = {
    new SocialAccount(
      UserId(rs.string("user_account_id")),
      SocialClientId.getByValue(rs.string("client_id")),
      SocialAccountId(rs.get("user_name")),
      SocialAccessToken(rs.string("access_token"), rs.string("access_token_secret"))
    )
  }

  override def getUserFromClient(clientId: SocialClientId, accessToken: SocialAccessToken): SocialAccount = clientId match {
    case Twitter => twitterUserGateway.getUser(accessToken.token, accessToken.secret)
    case _ => ???
  }
}
