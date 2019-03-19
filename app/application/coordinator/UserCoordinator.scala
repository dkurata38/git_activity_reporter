package application.coordinator

import application.cache.{SignInCache, SignUpCache}
import application.service.{GitAccountService, SocialAccountService, UserService, UserTokenService}
import domain.model.git.account.{AccessToken, GitAccount, GitClientId}
import domain.model.social.{SocialAccessToken, SocialAccount, SocialAccountId, SocialClientId}
import domain.model.user.User
import domain.model.user_token.{Token, UserToken}
import javax.inject.{Inject, Singleton}

@Singleton
class UserCoordinator @Inject()(private val socialAccountService: SocialAccountService, private val gitAccountService: GitAccountService, private val userService: UserService, private val userTokenService: UserTokenService) {
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
    val socialUser = new SocialAccount(signUpCache.userId, clientId, accountId, accessToken)
    socialAccountService.getBySocialAccountId(clientId, accountId) match {
      case Some(_) => Left("既に他のユーザでアカウント連携されています. このアカウントで利用するにはサインインをしてください.")
      case None =>  {
        Right(signUpCache.copy(socialAccount = Some(socialUser)))
      }
    }
  }

  def signUp(signUpCache: SignUpCache, clientId: GitClientId, userName: String, accessToken: AccessToken): Either[String, SignUpCache] = {
    val gitUser = new GitAccount(signUpCache.userId, clientId, userName, accessToken)
    gitAccountService.getByClientIdAndUserName(clientId, userName) match {
      case Some(_) => Left("既に他のユーザでアカウント連携されています. このアカウントで利用するにはサインインをしてください.")
      case None => {
        Right(signUpCache.copy(gitAccount = Some(gitUser)))
      }
    }
  }

  def registerTemporaryUser: User = {
    val user = User.createInstance
    userService.create(user)
    user
  }

  def activateUser(signUpCache: SignUpCache): Option[Token] = {
    val combinedOption = for {
      gitAccount <- signUpCache.gitAccount
      socialAccount <-signUpCache.socialAccount
    } yield(gitAccount, socialAccount)
    combinedOption.flatMap{ case(gitAccount, socialAccount) =>
      userService.getById(signUpCache.userId).map{user =>
        userService.updateUser(user.activate)
        gitAccountService.link(gitAccount)
        socialAccountService.link(socialAccount)
        val userToken = UserToken.issueTo(user.userId)
        userTokenService.create(userToken)
        userToken.token
      }
    }
  }
}
