package com.github.dkurata38.git_activity_reporter.application.coordinator

import com.github.dkurata38.git_activity_reporter.application.service.{SocialAccountService, UserService}
import com.github.dkurata38.git_activity_reporter.domain.`type`.SocialClientId
import com.github.dkurata38.git_activity_reporter.domain.model.git.GitAccount
import com.github.dkurata38.git_activity_reporter.domain.model.social.{SocialAccessToken, SocialAccount, SocialAccountId}
import com.github.dkurata38.git_activity_reporter.domain.model.user.{User, UserId}
import javax.inject.{Inject, Singleton}

@Singleton
class UserCoordinator @Inject() (private val socialAccountService: SocialAccountService, private val userService: UserService) {
  def signIn(gitAccount: GitAccount): UserId = ???

  def signIn(clientId: SocialClientId, accountId: SocialAccountId, accessToken: SocialAccessToken) :Option[UserId] = {
    val user = socialAccountService.getBySocialAccountId(clientId, accountId)
    user.map(u => u.userId)
  }

  def signUp(clientId: SocialClientId, accountId: SocialAccountId, accessToken: SocialAccessToken): UserId = {
    val user = User.newUser
    val registeredUser = userService.registerUser(user)

    val socialUser = new SocialAccount(registeredUser.userId, clientId, accountId, accessToken)
    socialAccountService.link(socialUser)
    user.userId
  }
}
