package com.github.dkurata38.git_activity_reporter.domain.social_account

import com.github.dkurata38.git_activity_reporter.domain.social_account.SocialAccount.SocialClientId

class SocialAccount(val userId: Int, val clientId: SocialClientId, val accessToken: String, val accessTokenSecret: String) {

}

object SocialAccount {
  sealed class SocialClientId(val clientId: String)
  case object Twitter extends SocialClientId("twitter")
}
