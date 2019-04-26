package adapter.gateway.github

import java.time.LocalDate

import domain.git_account.AccessToken
import domain.git_activity.PushActivities

trait GitHubActivitiesGateway {
  def getUserEvents(accessToken: AccessToken, from: LocalDate, to: LocalDate): PushActivities
}
