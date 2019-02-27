package domain.model.git.account

import domain.model.user.UserId

class GitAccount(val userId: UserId, val clientId: GitClientId, val gitUserName: String, val accessToken: AccessToken) {

}