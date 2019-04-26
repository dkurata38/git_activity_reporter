package domain.git_activity

import java.time.LocalDate

import domain.git_account.GitAccount

trait GitActivitiesRepository {
  def findByUserIdCreatedAtBetween(gitAccount: GitAccount, from: LocalDate, to: LocalDate): PushActivities
}
