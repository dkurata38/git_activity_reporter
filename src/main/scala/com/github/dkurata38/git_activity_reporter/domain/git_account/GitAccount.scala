package com.github.dkurata38.git_activity_reporter.domain.git_account

class GitAccount (private val userId: Int, val clientId: GitAccount.ClientId, val gitUserName: String, val accessToken: String) {

}

object GitAccount {
  sealed abstract class ClientId(val clientId: String)
  object ClientId {
    case object GitHub extends ClientId("GitHub")
  }
}