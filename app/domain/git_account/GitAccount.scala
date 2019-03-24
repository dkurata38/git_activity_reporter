package domain.git_account

import domain.user.UserId

class GitAccount(val userId: UserId, val clientId: GitClientId, val gitUserName: String, val accessToken: AccessToken) {

}