package application.repository

import java.time.LocalDate

import adapter.gateway.github.GitHubActivitiesGateway
import domain.git_account.GitAccount
import domain.git_account.GitClientId.GitHub
import domain.git_activity.{GitActivities, GitActivitiesRepository}
import javax.inject.{Inject, Singleton}

@Singleton
class GitActivitiesRepositoryImpl @Inject()(gitHubActivitiesGateway: GitHubActivitiesGateway) extends GitActivitiesRepository{
  override def findByUserIdCreatedAtBetween(gitAccount: GitAccount, to: LocalDate, from: LocalDate): GitActivities =
    gitAccount.clientId match {
      case GitHub => gitHubActivitiesGateway.getUserEvents(gitAccount.accessToken, to, from)
      case _ => ???
    }
}
