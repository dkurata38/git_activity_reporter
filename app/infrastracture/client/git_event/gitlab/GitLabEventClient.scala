package infrastracture.client.git_event.gitlab

import application.client.GitEventClient
import domain.model.git.{GitAccount, GitEvents}

class GitLabEventClient extends GitEventClient{
  override def getUserEvents(gitAccount: GitAccount): GitEvents = ???
}
