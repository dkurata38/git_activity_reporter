package application.interactor

import application.inputport.UserSignInUseCaseInputPort
import application.repository.IUserTokenRepository
import domain.model.git.account.{AccessToken, GitAccountRepository, GitClientId}
import domain.model.social.SocialClientId
import domain.model.user_token.{Token, UserToken}
import javax.inject.{Inject, Singleton}

@Singleton
class UserSignInUseCaseInteractor @Inject() (
      implicit private val userTokenRepository: IUserTokenRepository,
      private val gitAccountRepository: GitAccountRepository) extends UserSignInUseCaseInputPort{
  override def signInWith(clientId: GitClientId, accessToken: String): Either[String, Token] = {
    val gitAccount = gitAccountRepository.getUserFromClient(clientId, AccessToken(accessToken))
    gitAccountRepository.findByClientIdAndUserName(clientId, gitAccount.gitUserName).map{ u =>
      Right(UserToken.issueTo(u.userId).token)
    }.getOrElse(Left(""))
  }

  override def signInWith(clientId: SocialClientId, accessToken: String): Either[String, Token] = ???
}
