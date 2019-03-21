package application.cache

import domain.git_account.GitAccount
import domain.social.SocialAccount
import domain.user.UserId

case class SignUpCache(userId: UserId, gitAccount: Option[GitAccount] = None, socialAccount: Option[SocialAccount] = None) {
  def cacheGitAccount(gitAccount: GitAccount) = copy(gitAccount = Some(gitAccount))

  def cacheSocialAccount(socialAccount: SocialAccount) = copy(socialAccount = Some(socialAccount))
}

object SignUpCache {
  def createInstance(userId: UserId) = SignUpCache(userId)
}


