package domain.model.git.activity

import domain.model.git.account.GitClientId

case class GitRepository(gitClientId: GitClientId, repositoryId: GitRepositoryId, repositoryUrl: String) {

}
