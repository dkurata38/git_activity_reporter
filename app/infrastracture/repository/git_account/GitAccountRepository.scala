package infrastracture.repository.git_account

import application.repository.IGitAccountRepository
import domain.`type`.GitClientId
import domain.`type`.GitClientId.GitHub
import domain.model.git.GitAccount
import domain.model.user.UserId
import com.typesafe.config.ConfigFactory
import javax.inject.{Inject, Singleton}

@Singleton
class GitAccountRepository @Inject() extends IGitAccountRepository {
  val config = ConfigFactory.load()

  override def findAllByUserId(userId: UserId): Seq[GitAccount] = {
    val githubAccount = new GitAccount(UserId("1111"), GitHub, config.getString("app.github.user_name"), config.getString("app.github.access_token"))
    Seq(githubAccount)
  }

  override def findOneByUserIdAndGitProviderId(userId: UserId, clientId: GitClientId): Option[GitAccount] = ???
}
