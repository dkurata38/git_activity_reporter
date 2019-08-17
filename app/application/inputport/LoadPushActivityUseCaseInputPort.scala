package application.inputport

import domain.git.activity.PushActivities

trait LoadPushActivityUseCaseInputPort {
  def queryGitActivities(token: String): Either[String, PushActivities]
}
