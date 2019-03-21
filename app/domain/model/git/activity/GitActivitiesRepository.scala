package domain.model.git.activity

import java.time.LocalDate

import domain.model.git.account.GitAccount

trait GitActivitiesRepository {
  def findByUserIdCreatedAtBetween(gitAccount: GitAccount, from: LocalDate, to: LocalDate): GitActivities
}
