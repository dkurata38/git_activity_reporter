package infrastracture.client.git_event.bitbucket

import application.client.GitEventClient
import domain.model.git.{GitAccount, GitEvents}

class BitBucketEventClient extends GitEventClient{
  override def getUserEvents(gitAccount: GitAccount): GitEvents = ???
}
