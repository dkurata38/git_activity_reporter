package domain.git.activity

import java.time.LocalDate

import domain.git.account.GitAccount

trait GitActivitiesRepository {
  def findByUserIdCreatedAtBetween(gitAccount: GitAccount, from: LocalDate, to: LocalDate): PushActivities
}
