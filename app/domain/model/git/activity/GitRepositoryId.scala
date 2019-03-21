package domain.model.git.activity

case class GitRepositoryId(value: String) {
  def owner: String = value.split("/").apply(0)

  def repositoryName: String = value.split("/").apply(1)

  override def canEqual(that: Any): Boolean = this.getClass == that.getClass

  override def hashCode(): Int = value.hashCode

  override def equals(obj: Any): Boolean = {
    obj match {
      case obj: GitRepositoryId => canEqual(obj) && value == obj.value
      case _ => false
    }
  }
}

object GitRepositoryId {
  def of(owner: String, repositoryName: String) = new GitRepositoryId(owner.concat("/").concat(repositoryName))
}
