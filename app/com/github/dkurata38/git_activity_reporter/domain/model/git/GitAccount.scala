package com.github.dkurata38.git_activity_reporter.domain.model.git

import com.github.dkurata38.git_activity_reporter.domain.`type`.GitClientId
import com.github.dkurata38.git_activity_reporter.domain.model.user.UserId

class GitAccount (private val userId: UserId, val clientId: GitClientId, val gitUserName: String, val accessToken: String) {

}