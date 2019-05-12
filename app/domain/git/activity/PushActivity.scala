package domain.git.activity

class PushActivity(val gitRepository: GitRepository, val commitSHAs: Seq[String]){
  def commitCount: Int = commitSHAs.size
}
