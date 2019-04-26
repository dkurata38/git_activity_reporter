package domain.git_activity

class PushActivity(val gitRepository: GitRepository, val commitSHAs: Seq[String]){
  def commitCount = commitSHAs.size
}
