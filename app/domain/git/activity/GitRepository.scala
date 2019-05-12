package domain.git.activity

import domain.git.GitClientId

case class GitRepository(gitClientId: GitClientId, repositoryId: GitRepositoryId, url: String) {

}
