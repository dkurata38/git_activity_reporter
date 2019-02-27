package domain.model.git.event

sealed abstract class GitEventType

object GitEventType {

  case object Push extends GitEventType

  case object PullRequest extends GitEventType

}
