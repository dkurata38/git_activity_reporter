package domain.git.activity

import domain.git.account.GitAccount

trait PushActivityClient {
  def findByUserIdCreatedAtBetween(gitAccount: GitAccount): PushActivities
}
