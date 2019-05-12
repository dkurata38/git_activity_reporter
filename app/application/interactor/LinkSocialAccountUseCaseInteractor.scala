package application.interactor

import application.inputport.LinkSocialAccountUseCaseInputPort
import domain.social.{SocialAccessToken, SocialAccount, SocialAccountRepository, SocialClientId}
import domain.user.{Token, UserTokenRepository}
import javax.inject.{Inject, Singleton}

@Singleton
class LinkSocialAccountUseCaseInteractor @Inject() (
                                                     private val userTokenRepository: UserTokenRepository,
                                                     private val socialAccountRepository: SocialAccountRepository
                                                   ) extends LinkSocialAccountUseCaseInputPort{
  override def link(token: String, clientId: SocialClientId, accessToken: String, accessTokenSecret: String): Either[String, SocialAccount] = {
    userTokenRepository.findByUserToken(Token(token)).flatMap(userToken =>
      socialAccountRepository.getUserFromClient(clientId, SocialAccessToken(accessToken, accessTokenSecret)).flatMap(socialAccount =>
        socialAccountRepository.findOneBySocialAccountId(clientId, socialAccount.socialUserId) match {
          case None => Option{
            val socialUser = new SocialAccount(userToken.userId, clientId, socialAccount.socialUserId, socialAccount.accessToken)
            socialAccountRepository.create(socialUser)
            socialUser
          }
          case _ => None
        }
      )
    ).map(socialUser => Right(socialUser)).getOrElse(Left(""))
  }
}
