package com.github.dkurata38.git_activity_reporter.domain.git_event

import com.github.dkurata38.git_activity_reporter.domain.git_event.GitEvent.EventType

class GitEvent(val gitRepository: GitRepository, val eventType: EventType) {

}

object GitEvent {
  sealed abstract class EventType
  case object Push extends EventType
  case object PullRequest extends EventType
}
