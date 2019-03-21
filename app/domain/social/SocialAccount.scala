package domain.social

import domain.user.UserId

class SocialAccount(val userId: UserId, val clientId: SocialClientId, val socialUserId: SocialAccountId, val accessToken: SocialAccessToken) {

}