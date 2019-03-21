package adapter.gateway

import java.time.LocalDate

import domain.model.git.account.AccessToken
import domain.model.git.activity.GitActivities

trait GitHubActivitiesGateway {
  def getUserEvents(accessToken: AccessToken, from: LocalDate, to: LocalDate): GitActivities
}
