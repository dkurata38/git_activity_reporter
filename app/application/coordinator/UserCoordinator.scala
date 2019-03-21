package application.coordinator

import application.cache.SignUpCache
import application.service.{SocialAccountService, UserService, UserTokenService}
import domain.model.git.account.GitAccountRepository
import domain.model.social.{SocialAccessToken, SocialAccount, SocialAccountId, SocialClientId}
import domain.model.user.User
import javax.inject.{Inject, Singleton}

@Singleton
class UserCoordinator @Inject()(
                                 private val socialAccountService: SocialAccountService,
                                 private val userService: UserService,
                                 private val userTokenService: UserTokenService,
                                 private val gitAccountRepository: GitAccountRepository) {
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