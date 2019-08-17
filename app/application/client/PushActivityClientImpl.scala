package application.client

import domain.git.GitClientId.GitHub
import domain.git.account.GitAccount
import domain.git.activity.{PushActivities, PushActivityClient}
import javax.inject.{Inject, Singleton}

@Singleton
class PushActivityClientImpl @Inject() extends PushActivityClient{
  override def findByUserIdCreatedAtBetween(gitAccount: GitAccount): PushActivities = {
    gitAccount.clientId match {
      case GitHub => ???
      case _ => ???
    }
  }
}
