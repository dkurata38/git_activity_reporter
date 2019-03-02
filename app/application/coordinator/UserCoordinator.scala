package application.coordinator

import application.cache.SignInCache
import application.service.{GitAccountService, SocialAccountService, UserService}
import domain.model.git.account.{AccessToken, GitAccount, GitClientId}
import domain.model.social.{SocialAccessToken, SocialAccount, SocialAccountId, SocialClientId}
import domain.model.user.{User, UserId}
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

  def signUp(signInCache: SignInCache,clientId: SocialClientId, accountId: SocialAccountId, accessToken: SocialAccessToken): Either[String, SignInCache] = {
    userService.getById(signInCache.user.userId).map { u =>
      val socialUser = new SocialAccount(u.userId, clientId, accountId, accessToken)
      socialAccountService.getBySocialAccountId(clientId, accountId) match {
        case Some(_) => Left("既に他のユーザでアカウント連携されています. このアカウントで利用するにはサインインをしてください.")
        case None =>  {
          socialAccountService.link(socialUser)
          Right(signInCache.addSocialOauthInfo(clientId, accessToken))
        }
      }
    }.getOrElse(Left("会員登録/ログインを最初からやり直してください."))
  }

  def signUp(signInCache: SignInCache, clientId: GitClientId, userName: String, accessToken: AccessToken): Either[String, SignInCache] = {
    userService.getById(signInCache.user.userId).map{u =>
      val gitUser = new GitAccount(u.userId, clientId, userName, accessToken)
      gitAccountService.getByClientIdAndUserName(clientId, userName) match {
        case Some(_) => Left("既に他のユーザでアカウント連携されています. このアカウントで利用するにはサインインをしてください.")
        case None => {
          gitAccountService.link(gitUser)
          Right(signInCache.addGitOauthInfo(clientId, accessToken))
        }
      }
    }.getOrElse(Left("会員登録/ログインを最初からやり直してください."))
  }

  def registerNewUser: User = {
    val user = User.newUser
    userService.createUser(user)
    user
  }

  def activateUser(userId: UserId): UserId = {
    val user = userService.getById(userId)
    user.map(u => userService.updateUser(u.activate))
    user.map(u => u.userId).orNull
  }
}
