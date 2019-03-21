package application.repository

import java.time.LocalDate

import adapter.gateway.GitHubActivitiesGateway
import domain.model.git.account.GitAccount
import domain.model.git.account.GitClientId.GitHub
import domain.model.git.activity.{GitActivities, GitActivitiesRepository}
import javax.inject.{Inject, Singleton}

@Singleton
class GitActivitiesRepositoryImpl @Inject()(gitHubActivitiesGateway: GitHubActivitiesGateway) extends GitActivitiesRepository{
  override def findByUserIdCreatedAtBetween(gitAccount: GitAccount, to: LocalDate, from: LocalDate): GitActivities =
    gitAccount.clientId match {
      case GitHub => gitHubActivitiesGateway.getUserEvents(gitAccount.accessToken, to, from)
      case _ => ???
    }
}
