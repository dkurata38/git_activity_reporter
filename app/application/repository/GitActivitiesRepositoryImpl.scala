package application.repository

import java.time.LocalDate

import adapter.gateway.github.GitHubActivitiesGateway
import domain.git.PushActivities
import domain.git.GitClientId.GitHub
import domain.git.account.GitAccount
import domain.git.activity.{GitActivitiesRepository, PushActivities}
import domain.git_activity.GitActivitiesRepository
import javax.inject.{Inject, Singleton}

@Singleton
class GitActivitiesRepositoryImpl @Inject()(gitHubActivitiesGateway: GitHubActivitiesGateway) extends GitActivitiesRepository{
  override def findByUserIdCreatedAtBetween(gitAccount: GitAccount, from: LocalDate, to: LocalDate): PushActivities =
    gitAccount.clientId match {
      case GitHub => gitHubActivitiesGateway.getUserEvents(gitAccount.accessToken, from, to)
      case _ => ???
    }
}
