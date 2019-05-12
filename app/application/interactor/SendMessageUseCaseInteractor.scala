package application.interactor

import application.inputport.SendMessageUseCaseInputPort
import domain.social.SocialClientId.Twitter
import domain.social.{SocialAccountRepository, SocialClientId, SocialMessage, SocialMessageRepository}
import domain.user.{Token, UserTokenRepository}
import javax.inject.{Inject, Singleton}

@Singleton
class SendMessageUseCaseInteractor @Inject()(
                                              private val userTokenRepository: UserTokenRepository,
                                              private val socialAccountRepository: SocialAccountRepository,
                                              private val socialMessageRepository: SocialMessageRepository
                            ) extends SendMessageUseCaseInputPort{
  override def sendMessage(token: String, clientId: SocialClientId, message: String) = {
    userTokenRepository.findByUserToken(Token(token)).flatMap{ userToken =>
      socialAccountRepository.findOneByUserIdAndSocialClientId(userToken.userId, Twitter)
    }.foreach{ socialAccount =>
      socialMessageRepository.send(socialAccount, SocialMessage(message))
    }
  }
}
