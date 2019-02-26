package controllers

import domain.model.git.GitAccount
import domain.model.social.SocialAccount
import domain.model.user.User

case class SignUpCache(var user: User, var gitAccount: Option[GitAccount], var socialAccount: Option[SocialAccount]) {
}



