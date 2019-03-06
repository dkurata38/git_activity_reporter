package application.coordinator

import application.cache.{SignInCache, SignUpCache}
import application.service.{GitAccountService, SocialAccountService, UserService}
import domain.model.git.account.{AccessToken, GitAccount, GitClientId}
import domain.model.social.{SocialAccessToken, SocialAccount, SocialAccountId, SocialClientId}
import domain.model.user.User
import javax.inject.{Inject, Singleton}

@Singleton
class UserCoordinator @Inject()(private val socialAccountService: SocialAccountService, private val gitAccountService: GitAccountService, private val userService: UserService) {
  def signIn(clientId: GitClientId, userName: String, accessToken: AccessToken): Option[SignInCache] = {
    gitAccountService.getByClientIdAndUserName(clientId, userName).map{ u =>
      new SignInCache(
        userService.getById(u.userId).get,
        gitAccountService.getAllByUserId(u.userId).map(g => (g.clientId, g.accessToken)).toMap,
        socialAccountService.getAllByUserId(u.userId).map(s => (s.clientId, s.accessToken)).toMap
      )
    }
  }

  def signIn(clientId: SocialClientId, accountId: SocialAccountId, accessToken: SocialAccessToken): Option[SignInCache] = {
    socialAccountService.getBySocialAccountId(clientId, accountId).map{u =>
      new SignInCache(
        userService.getById(u.userId).get,
        gitAccountService.getAllByUserId(u.userId).map(g => (g.clientId, g.accessToken)).toMap,
        socialAccountService.getAllByUserId(u.userId).map(s => (s.clientId, s.accessToken)).toMap
      )
    }
  }

  def signUp(signUpCache: SignUpCache, clientId: SocialClientId, accountId: SocialAccountId, accessToken: SocialAccessToken): Either[String, SignUpCache] = {
    val socialUser = new SocialAccount(signUpCache.user.userId, clientId, accountId, accessToken)
    socialAccountService.getBySocialAccountId(clientId, accountId) match {
      case Some(_) => Left("既に他のユーザでアカウント連携されています. このアカウントで利用するにはサインインをしてください.")
      case None =>  {
        Right(signUpCache.copy(socialAccount = Some(socialUser)))
      }
    }
  }

  def signUp(signUpCache: SignUpCache, clientId: GitClientId, userName: String, accessToken: AccessToken): Either[String, SignUpCache] = {
    val gitUser = new GitAccount(signUpCache.user.userId, clientId, userName, accessToken)
    gitAccountService.getByClientIdAndUserName(clientId, userName) match {
      case Some(_) => Left("既に他のユーザでアカウント連携されています. このアカウントで利用するにはサインインをしてください.")
      case None => {
        Right(signUpCache.copy(gitAccount = Some(gitUser)))
      }
    }
  }

  def registerNewUser: User = {
    val user = User.newUser
    userService.createUser(user)
    user
  }

  def activateUser(signUpCache: SignUpCache): Option[String] = {
    for {
      gitAccount <- signUpCache.gitAccount
      socialAccount <-signUpCache.socialAccount
    } yield {
      userService.createUser(signUpCache.user.activate)
      gitAccountService.link(gitAccount)
      socialAccountService.link(socialAccount)
      signUpCache.user.userId.value
    }
  }
}
