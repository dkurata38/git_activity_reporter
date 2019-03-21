package application.coordinator

import application.cache.{SignInCache, SignUpCache}
import application.service.{GitAccountService, SocialAccountService, UserService, UserTokenService}
import domain.model.git.account.GitAccountRepository
import domain.model.social.{SocialAccessToken, SocialAccount, SocialAccountId, SocialClientId}
import domain.model.user.User
import javax.inject.{Inject, Singleton}

@Singleton
class UserCoordinator @Inject()(
                                 private val socialAccountService: SocialAccountService,
                                 private val gitAccountService: GitAccountService,
                                 private val userService: UserService,
                                 private val userTokenService: UserTokenService,
                                 private val gitAccountRepository: GitAccountRepository) {
  def signIn(clientId: SocialClientId, accountId: SocialAccountId, accessToken: SocialAccessToken): Option[SignInCache] = {
    socialAccountService.getBySocialAccountId(clientId, accountId).map{u =>
      new SignInCache(
        userService.getById(u.userId).get,
        gitAccountRepository.findAllByUserId(u.userId).map(g => (g.clientId, g.accessToken)).toMap,
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

  def registerTemporaryUser: User = {
    val user = User.createInstance
    userService.create(user)
    user
  }
}