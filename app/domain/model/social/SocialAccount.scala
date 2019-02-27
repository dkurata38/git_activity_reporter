package domain.model.social

import domain.model.user.UserId

class SocialAccount(val userId: UserId, val clientId: SocialClientId, val socialUserId: SocialAccountId, val accessToken: SocialAccessToken) {

}