package com.github.dkurata38.git_activity_reporter.infrastracture.client.git_event.bitbucket

import com.github.dkurata38.git_activity_reporter.application.client.GitEventClient
import com.github.dkurata38.git_activity_reporter.domain.model.git.{GitAccount, GitEvents}

class BitBucketEventClient extends GitEventClient{
  override def getUserEvents(gitAccount: GitAccount): GitEvents = ???
}
