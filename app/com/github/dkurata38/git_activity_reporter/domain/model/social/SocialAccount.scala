package com.github.dkurata38.git_activity_reporter.domain.model.social

import com.github.dkurata38.git_activity_reporter.domain.`type`.SocialClientId
import com.github.dkurata38.git_activity_reporter.domain.model.user.UserId

class SocialAccount(val userId: UserId, val clientId: SocialClientId, val socialUserId: SocialAccountId, val accessToken: SocialAccessToken) {

}