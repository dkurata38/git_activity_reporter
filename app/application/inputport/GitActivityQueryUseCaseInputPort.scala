package application.inputport

import domain.git_activity.PushActivities

trait GitActivityQueryUseCaseInputPort {
  def queryGitActivities(token: String): PushActivities
}
