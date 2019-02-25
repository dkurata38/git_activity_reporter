package com.github.dkurata38.git_activity_reporter.controllers

import com.github.dkurata38.git_activity_reporter.domain.model.git.GitAccount
import com.github.dkurata38.git_activity_reporter.domain.model.social.SocialAccount
import com.github.dkurata38.git_activity_reporter.domain.model.user.User

case class SignUpCache(var user: User, var gitAccount: Option[GitAccount], var socialAccount: Option[SocialAccount]) {
}



