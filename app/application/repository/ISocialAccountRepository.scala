package application.repository

import domain.model.social.{SocialAccount, SocialAccountId, SocialClientId}
import domain.model.user.UserId

trait ISocialAccountRepository {
  def findAllByUserId(userId: UserId): Seq[SocialAccount]

  def findOneByUserIdAndSocialClientId(userId: UserId, clientId: SocialClientId): Option[SocialAccount]

  def findOneBySocialAccountId(clientId: SocialClientId, accountId: SocialAccountId): Option[SocialAccount]

  def create(socialAccount: SocialAccount): SocialAccount
}
