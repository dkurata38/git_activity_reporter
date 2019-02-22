package com.github.dkurata38.git_activity_reporter.domain.model.git

import com.github.dkurata38.git_activity_reporter.domain.`type`.GitClientId

class GitAccount (private val userId: Int, val clientId: GitClientId, val gitUserName: String, val accessToken: String) {

}