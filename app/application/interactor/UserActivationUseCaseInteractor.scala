package application.interactor

import application.inputport.UserActivationUseCaseInputPort
import application.repository.UserRepositoryImpl
import domain.git_account.GitAccountRepository
import domain.social.SocialAccountRepository
import domain.user_token.{Token, UserTokenRepository}
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
}
