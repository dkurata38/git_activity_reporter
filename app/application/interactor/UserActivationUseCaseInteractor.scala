package application.interactor

import application.inputport.UserActivationUseCaseInputPort
import application.repository.UserRepositoryImpl
import domain.git.account.{AccessToken, GitAccount, GitAccountRepository}
import domain.git.{GitAccount, GitClientId}
import domain.git_account.GitAccount
import domain.social.SocialAccountRepository
import domain.user._
import javax.inject.{Inject, Singleton}

@Singleton
class UserActivationUseCaseInteractor @Inject() (
                                                  private val gitAccountRepository: GitAccountRepository,
                                                  private val socialAccountRepository: SocialAccountRepository,
                                                  private val userRepository: UserRepositoryImpl,
                                                  implicit private val userTokenRepository: UserTokenRepository
                                                ) extends UserActivationUseCaseInputPort{
  override def activate(token: String): Either[String, Token] = {
    userTokenRepository.findByUserToken(Token(token)).flatMap{ userToken =>
      gitAccountRepository.findAllByUserId(userToken.userId).find(_ => true).flatMap {_ =>
        socialAccountRepository.findAllByUserId(userToken.userId).find(_ => true).flatMap { _ =>
          userRepository.findOneById(userToken.userId).map { user =>
            userRepository.update(user.activate)
            userToken.reIssue.token
          }
        }
      }
    }
  }.map(token => Right(token)).getOrElse(Left(""))

  override def register(token: String, gitClientId: GitClientId, accessToken: AccessToken): Either[String, Token] = {
    gitAccountRepository.getUserFromClient(gitClientId, accessToken).map{gitAccount =>
      val userId = UserId.newId
      userRepository.create(new User(userId, RegistrationStatus.Regular))
      gitAccountRepository.create(new GitAccount(userId, gitClientId, gitAccount.gitUserName, accessToken))
      userTokenRepository.create(new UserToken(userId, Token(token)))
      Token(token)
    }.toRight("ユーザ登録に失敗しました")
  }
}
