package application.inputport

import domain.git.activity.PushActivities

trait GitActivityQueryUseCaseInputPort {
  def queryGitActivities(token: String): PushActivities
}
