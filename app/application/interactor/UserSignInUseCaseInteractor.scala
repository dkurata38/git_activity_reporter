package application.interactor

import application.inputport.UserSignInUseCaseInputPort
import domain.model.git.account.{AccessToken, GitAccountRepository, GitClientId}
import domain.model.social.SocialClientId.Twitter
import domain.model.social.{SocialAccessToken, SocialAccountRepository, SocialClientId}
import domain.model.user_token.{Token, UserToken, UserTokenRepository}
import javax.inject.{Inject, Singleton}

@Singleton
class UserSignInUseCaseInteractor @Inject() (
                                              implicit private val userTokenRepository: UserTokenRepository,
                                              private val gitAccountRepository: GitAccountRepository,
                                              private val socialAccountRepository: SocialAccountRepository) extends UserSignInUseCaseInputPort{
  override def signInWith(clientId: GitClientId, accessToken: String): Either[String, Token] = {
    val gitAccount = gitAccountRepository.getUserFromClient(clientId, AccessToken(accessToken))
    gitAccountRepository.findByClientIdAndUserName(clientId, gitAccount.gitUserName).map{ u =>
      Right(UserToken.issueTo(u.userId).token)
    }.getOrElse(Left(""))
  }

  override def signInWith(clientId: SocialClientId, accessToken: String, accessTokenSecret: String): Either[String, Token] = {
    val socialAccount = socialAccountRepository.getUserFromClient(Twitter, SocialAccessToken(accessToken, accessTokenSecret))
    socialAccountRepository.findOneBySocialAccountId(clientId, socialAccount.socialUserId).map{ u =>
      Right(UserToken.issueTo(u.userId).token)
    }.getOrElse(Left(""))
  }
}
