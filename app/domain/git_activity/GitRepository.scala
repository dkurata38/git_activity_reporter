package domain.git_activity

import domain.git_account.GitClientId

case class GitRepository(gitClientId: GitClientId, repositoryId: GitRepositoryId, repositoryUrl: String) {

}
