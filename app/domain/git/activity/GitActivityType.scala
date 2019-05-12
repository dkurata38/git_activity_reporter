package domain.git.activity

sealed abstract class GitActivityType

object GitActivityType {

  case object Push extends GitActivityType

  case object PullRequest extends GitActivityType

}
