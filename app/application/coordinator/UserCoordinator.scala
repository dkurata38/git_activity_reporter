package application.coordinator

import application.service.{SocialAccountService, UserService}
import domain.`type`.SocialClientId
import domain.model.git.GitAccount
import domain.model.social.{SocialAccessToken, SocialAccount, SocialAccountId}
import domain.model.user.{User, UserId}
import javax.inject.{Inject, Singleton}

@Singleton
class UserCoordinator @Inject()(private val socialAccountService: SocialAccountService, private val userService: UserService) {
  def signIn(gitAccount: GitAccount): UserId = ???

  def signIn(clientId: SocialClientId, accountId: SocialAccountId, accessToken: SocialAccessToken): Option[UserId] = {
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
