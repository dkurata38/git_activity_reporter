package domain.git_activity

import domain.git_activity.GitActivityType.Push

class PushActivity(gitRepository: GitRepository, val commitSHAs: Seq[String]) extends GitActivity (gitRepository, Push){

}
