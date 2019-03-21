package application.interactor

import application.inputport.UserActivationUseCaseInputPort
import application.repository.IUserTokenRepository
import application.service.SocialAccountService
import domain.model.git.account.GitAccountRepository
import domain.model.user_token.Token
import infrastracture.repository.UserRepository
import javax.inject.{Inject, Singleton}

@Singleton
class UserActivationUseCaseInteractor @Inject() (
      private val gitAccountRepository: GitAccountRepository,
      private val socialAccountService: SocialAccountService,
      private val userRepository: UserRepository,
      implicit private val userTokenRepository: IUserTokenRepository
                                                ) extends UserActivationUseCaseInputPort{
  override def activate(token: String): Either[String, Token] = {
    userTokenRepository.findByUserToken(Token(token)).flatMap{ userToken =>
      gitAccountRepository.findAllByUserId(userToken.userId).find(_ => true).flatMap {_ =>
        socialAccountService.getAllByUserId(userToken.userId).find(_ => true).flatMap { _ =>
          userRepository.findOneById(userToken.userId).map { user =>
            userRepository.update(user.activate)
            userToken.reIssue.token
          }
        }
      }
    }
  }.map(token => Right(token)).getOrElse(Left(""))
}
