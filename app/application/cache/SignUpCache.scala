package application.cache

import domain.model.git.account.GitAccount
import domain.model.social.SocialAccount
import domain.model.user.UserId

case class SignUpCache(userId: UserId, gitAccount: Option[GitAccount] = None, socialAccount: Option[SocialAccount] = None) {
  def cacheGitAccount(gitAccount: GitAccount) = copy(gitAccount = Some(gitAccount))

  def cacheSocialAccount(socialAccount: SocialAccount) = copy(socialAccount = Some(socialAccount))
}

object SignUpCache {
  def createInstance(userId: UserId) = SignUpCache(userId)
}


