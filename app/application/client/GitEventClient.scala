package application.client

import domain.model.git.{GitAccount, GitEvents}

trait GitEventClient {
  def getUserEvents(gitAccount: GitAccount): GitEvents
}
