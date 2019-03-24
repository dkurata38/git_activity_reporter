package application.inputport

import domain.git_activity.GitActivities

trait GitActivityQueryUseCaseInputPort {
  def queryGitActivities(token: String): GitActivities
}
