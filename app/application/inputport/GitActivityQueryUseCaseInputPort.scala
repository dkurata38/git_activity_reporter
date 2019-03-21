package application.inputport

import domain.model.git.activity.GitActivities

trait GitActivityQueryUseCaseInputPort {
  def queryGitActivities(token: String): GitActivities
}
