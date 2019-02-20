package com.github.dkurata38.git_activity_reporter.domain.`type`

case class GitRepositoryId(value: String) {
  def owner: String = value.split("/").apply(0)
  def repositoryName: String = value.split("/").apply(1)
}

object GitRepositoryId {
  def of(owner: String, repositoryName: String) = new GitRepositoryId(owner.concat("/").concat(repositoryName))
}
