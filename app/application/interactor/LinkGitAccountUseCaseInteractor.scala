package application.interactor

import application.inputport.LinkGitAccountUseCaseInputPort
import application.repository.IUserTokenRepository
import domain.model.git.account.{AccessToken, GitAccount, GitAccountRepository, GitClientId}
import domain.model.user_token.Token
import javax.inject.{Inject, Singleton}

@Singleton
class LinkGitAccountUseCaseInteractor @Inject()(
     private val gitAccountRepository: GitAccountRepository,
     implicit private val userTokenRepository: IUserTokenRepository) extends LinkGitAccountUseCaseInputPort{
  override def link(token: String, clientId: GitClientId, accessToken: String): Either[String, GitAccount] = {
    userTokenRepository.findByUserToken(Token(token)).flatMap{ token =>
      gitAccountRepository.getUserFromClient(clientId, AccessToken(accessToken)).flatMap{gitAccount =>
        gitAccountRepository.findByClientIdAndUserName(clientId,gitAccount.gitUserName) match {
          case None => Option{
            val gitUser = new GitAccount(token.userId, gitAccount.clientId, gitAccount.gitUserName, gitAccount.accessToken)
            gitAccountRepository.create(gitUser)
            gitUser
          }
          case _ => None
        }
      }
    }
  }.map{gitUser => Right(gitUser)}.getOrElse(Left(""))
}
