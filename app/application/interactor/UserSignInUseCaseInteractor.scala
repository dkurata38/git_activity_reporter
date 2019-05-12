package application.interactor

import application.inputport.UserSignInUseCaseInputPort
import domain.git.account.{AccessToken, GitAccount, GitAccountRepository}
import domain.git.{GitAccount, GitClientId}
import domain.git_account.GitAccount
import domain.social.SocialClientId.Twitter
import domain.social.{SocialAccessToken, SocialAccountRepository, SocialClientId}
import domain.user.RegistrationStatus.Regular
import domain.user._
import javax.inject.{Inject, Singleton}

@Singleton
class UserSignInUseCaseInteractor @Inject() (
                                              implicit private val userTokenRepository: UserTokenRepository,
                                              private val userRepository: UserRepository,
                                              private val gitAccountRepository: GitAccountRepository,
                                              private val socialAccountRepository: SocialAccountRepository) extends UserSignInUseCaseInputPort{
  override def signInWith(clientId: GitClientId, accessToken: String): Either[String, Token] = {
    gitAccountRepository.getUserFromClient(clientId, AccessToken(accessToken))
      .map(gitAccount => gitAccountRepository.findByClientIdAndUserName(clientId, gitAccount.gitUserName)
        .map(u => Right(UserToken.issueTo(u.userId).token))
        .getOrElse{
          val userId = UserId.newId
          userRepository.create(new User(userId, Regular))
          gitAccountRepository.create(new GitAccount(userId, clientId, gitAccount.gitUserName, AccessToken(accessToken)))
          Right(UserToken.issueTo(userId).token)
        }).getOrElse(Left("サインインに失敗しました"))
  }

  override def signInWith(clientId: SocialClientId, accessToken: String, accessTokenSecret: String): Either[String, Token] = {
    socialAccountRepository.getUserFromClient(Twitter, SocialAccessToken(accessToken, accessTokenSecret)).flatMap(socialAccount =>
      socialAccountRepository.findOneBySocialAccountId(clientId, socialAccount.socialUserId)
    )
    .map{ u =>
      Right(UserToken.issueTo(u.userId).token)
    }.getOrElse(Left("サインインに失敗しました"))
  }
}
