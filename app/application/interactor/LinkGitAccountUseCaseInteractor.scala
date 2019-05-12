package application.interactor

import application.inputport.LinkGitAccountUseCaseInputPort
import domain.git.account.{AccessToken, GitAccount, GitAccountRepository}
import domain.git.{GitAccount, GitClientId}
import domain.git_account.GitAccount
import domain.user.{Token, UserTokenRepository}
import javax.inject.{Inject, Singleton}

@Singleton
class LinkGitAccountUseCaseInteractor @Inject()(
                                                 private val gitAccountRepository: GitAccountRepository,
                                                 implicit private val userTokenRepository: UserTokenRepository) extends LinkGitAccountUseCaseInputPort{
  override def link(token: String, clientId: GitClientId, accessToken: AccessToken): Either[String, GitAccount] = {
    userTokenRepository.findByUserToken(Token(token)).flatMap{ token =>
      gitAccountRepository.getUserFromClient(clientId, accessToken).flatMap{ gitAccount =>
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
