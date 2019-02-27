package application.coordinator

import application.cache.SignInCache
import application.service.{SocialAccountService, UserService}
import domain.model.git.account.GitAccount
import domain.model.social.{SocialAccessToken, SocialAccount, SocialAccountId, SocialClientId}
import domain.model.user.{User, UserId}
import javax.inject.{Inject, Singleton}

@Singleton
class UserCoordinator @Inject()(private val socialAccountService: SocialAccountService, private val userService: UserService) {
  def signIn(gitAccount: GitAccount): UserId = ???

  def signIn(clientId: SocialClientId, accountId: SocialAccountId, accessToken: SocialAccessToken): Option[SignInCache] = {
    val user = socialAccountService.getBySocialAccountId(clientId, accountId)
    user.map(u => SignInCache(u.userId))
  }

  def signUp(clientId: SocialClientId, accountId: SocialAccountId, accessToken: SocialAccessToken): UserId = {
    val user = User.newUser
    userService.createUser(user)

    val socialUser = new SocialAccount(user.userId, clientId, accountId, accessToken)
    socialAccountService.link(socialUser)
    user.userId
  }

  def registerNewUser: UserId = {
    val user = User.newUser
    userService.createUser(user)
    user.userId
  }

  def activateUser(userId: UserId): UserId = {
    val user = userService.getById(userId)
    user.map(u => userService.updateUser(u.activate))
    user.map(u => u.userId).orNull
  }
}
