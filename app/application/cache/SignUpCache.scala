package application.cache

import domain.model.git.account.GitAccount
import domain.model.social.SocialAccount
import domain.model.user.User

case class SignUpCache(user: User, gitAccount: Option[GitAccount] = None, socialAccount: Option[SocialAccount] = None) {
  def cacheGitAccount(gitAccount: GitAccount) = copy(gitAccount = Some(gitAccount))

  def cacheSocialAccount(socialAccount: SocialAccount) = copy(socialAccount = Some(socialAccount))
}

object SignUpCache {
  def createInstance = SignUpCache(User.newUser)
}


