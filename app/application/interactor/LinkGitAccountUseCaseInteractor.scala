package application.interactor

import application.inputport.LinkGitAccountUseCaseInputPort
import application.repository.IUserTokenRepository
import domain.model.git.account.{AccessToken, GitAccount, GitAccountRepository, GitClientId}
import javax.inject.{Inject, Singleton}

@Singleton
class LinkGitAccountUseCaseInteractor @Inject()(
     private val gitAccountRepository: GitAccountRepository,
     implicit private val userTokenRepository: IUserTokenRepository) extends LinkGitAccountUseCaseInputPort{
  override def link(token: String, clientId: GitClientId, accessToken: String): Either[String, GitAccount] = {
    val gitAccount = gitAccountRepository.getUserFromClient(clientId, AccessToken(accessToken))
    gitAccountRepository.findByClientIdAndUserName(clientId,gitAccount.gitUserName) match {
      case None => Right{
        gitAccountRepository.create(gitAccount)
        gitAccount
      }
      case _ => Left("")
    }
  }
}
