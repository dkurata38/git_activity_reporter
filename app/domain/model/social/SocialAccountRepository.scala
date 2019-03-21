package domain.model.social

import domain.model.user.UserId

trait SocialAccountRepository {
  def findAllByUserId(userId: UserId): Seq[SocialAccount]

  def findOneByUserIdAndSocialClientId(userId: UserId, clientId: SocialClientId): Option[SocialAccount]

  def findOneBySocialAccountId(clientId: SocialClientId, accountId: SocialAccountId): Option[SocialAccount]

  def create(socialAccount: SocialAccount): Int

  def getUserFromClient(clientId: SocialClientId, accessToken: SocialAccessToken): SocialAccount
}
