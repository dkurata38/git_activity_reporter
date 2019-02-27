package application.cache

import domain.model.git.account.GitAccount
import domain.model.social.SocialAccount
import domain.model.user.UserId

case class SignUpCache(var userId: UserId, var gitAccount: Option[GitAccount], var socialAccount: Option[SocialAccount]) {
}
