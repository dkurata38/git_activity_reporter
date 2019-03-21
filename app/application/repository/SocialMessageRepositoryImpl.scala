package application.repository

import domain.model.social.{SocialAccount, SocialMessage, SocialMessageRepository}
import javax.inject.{Inject, Singleton}

@Singleton
class SocialMessageRepositoryImpl @Inject() extends SocialMessageRepository{
  override def send(socialAccount: SocialAccount, socialMessage: SocialMessage): Unit = ???
}
