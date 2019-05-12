package adapter.gateway.github

import java.time.LocalDate

import domain.git.account.AccessToken
import domain.git.activity.PushActivities

trait GitHubActivitiesGateway {
  def getUserEvents(accessToken: AccessToken, from: LocalDate, to: LocalDate): PushActivities
}
