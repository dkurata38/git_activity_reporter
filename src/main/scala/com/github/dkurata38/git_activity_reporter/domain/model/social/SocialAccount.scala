package com.github.dkurata38.git_activity_reporter.domain.model.social

import com.github.dkurata38.git_activity_reporter.domain.`type`.SocialClientId

class SocialAccount(val userId: Int, val clientId: SocialClientId, val accessToken: String, val accessTokenSecret: String) {

}