package application.interactor

import application.inputport.UserSignInUseCaseInputPort
import domain.git_account.{AccessToken, GitAccountRepository, GitClientId}
import domain.social.SocialClientId.Twitter
import domain.social.{SocialAccessToken, SocialAccountRepository, SocialClientId}
import domain.user_token.{Token, UserToken, UserTokenRepository}
import javax.inject.{Inject, Singleton}

@Singleton
class UserSignInUseCaseInteractor @Inject() (
                                              implicit private val userTokenRepository: UserTokenRepository,
                                              private val gitAccountRepository: GitAccountRepository,
                                              private val socialAccountRepository: SocialAccountRepository) extends UserSignInUseCaseInputPort{
  override def signInWith(clientId: GitClientId, accessToken: String): Either[String, Token] = {
    gitAccountRepository.getUserFromClient(clientId, AccessToken(accessToken)).flatMap(gitAccount =>
      gitAccountRepository.findByClientIdAndUserName(clientId, gitAccount.gitUserName)
    )
    .map{ u =>
      Right(UserToken.issueTo(u.userId).token)
    }.getOrElse(Left(""))
  }

  override def signInWith(clientId: SocialClientId, accessToken: String, accessTokenSecret: String): Either[String, Token] = {
    socialAccountRepository.getUserFromClient(Twitter, SocialAccessToken(accessToken, accessTokenSecret)).flatMap(socialAccount =>
      socialAccountRepository.findOneBySocialAccountId(clientId, socialAccount.socialUserId)
    )
    .map{ u =>
      Right(UserToken.issueTo(u.userId).token)
    }.getOrElse(Left(""))
  }
}
