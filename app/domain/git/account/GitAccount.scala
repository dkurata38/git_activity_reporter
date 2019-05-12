package domain.git.account

import domain.git.GitClientId
import domain.user.UserId

class GitAccount(val userId: UserId, val clientId: GitClientId, val gitUserName: String, val accessToken: AccessToken) {

}