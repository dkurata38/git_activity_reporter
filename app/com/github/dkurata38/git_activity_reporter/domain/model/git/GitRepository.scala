package com.github.dkurata38.git_activity_reporter.domain.model.git

import com.github.dkurata38.git_activity_reporter.domain.`type`.{GitClientId, GitRepositoryId}

case class GitRepository(gitClientId: GitClientId, repositoryId: GitRepositoryId, repositoryUrl: String) {

}
