package domain.model.git

import domain.`type`.{GitClientId, GitRepositoryId}

case class GitRepository(gitClientId: GitClientId, repositoryId: GitRepositoryId, repositoryUrl: String) {

}
