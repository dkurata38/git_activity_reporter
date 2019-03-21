package application.coordinator

import application.repository.IUserTokenRepository
import domain.model.social.SocialClientId.Twitter
import domain.model.social.{SocialAccountRepository, SocialClientId, SocialMessage, SocialMessageRepository}
import domain.model.user_token.Token
import javax.inject.{Inject, Singleton}

@Singleton
class SendMessageCoordinator @Inject() (
                                         private val userTokenRepository: IUserTokenRepository,
                                         private val socialAccountRepository: SocialAccountRepository,
                                         private val socialMessageRepository: SocialMessageRepository
                            ) {
  def sendMessage(token: String, clientId: SocialClientId, message: String) = {
    userTokenRepository.findByUserToken(Token(token)).flatMap{ userToken =>
      socialAccountRepository.findOneByUserIdAndSocialClientId(userToken.userId, Twitter)
    }.foreach{ socialAccount =>
      socialMessageRepository.send(socialAccount, SocialMessage(message))
    }
  }
}
