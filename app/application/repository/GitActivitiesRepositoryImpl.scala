package application.repository

import java.time.LocalDate

import adapter.gateway.github.GitHubActivitiesGateway
import domain.git_account.GitAccount
import domain.git_account.GitClientId.GitHub
import domain.git_activity.{PushActivities, GitActivitiesRepository}
import javax.inject.{Inject, Singleton}

@Singleton
class GitActivitiesRepositoryImpl @Inject()(gitHubActivitiesGateway: GitHubActivitiesGateway) extends GitActivitiesRepository{
  override def findByUserIdCreatedAtBetween(gitAccount: GitAccount, from: LocalDate, to: LocalDate): PushActivities =
    gitAccount.clientId match {
      case GitHub => gitHubActivitiesGateway.getUserEvents(gitAccount.accessToken, from, to)
      case _ => ???
    }
}
