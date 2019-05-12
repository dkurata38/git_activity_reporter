package application.inputport

import domain.git.GitClientId
import domain.social.SocialClientId
import domain.user.Token

trait UserSignInUseCaseInputPort {
  def signInWith(clientId: GitClientId, accessToken: String): Either[String, Token]

  def signInWith(clientId: SocialClientId, accessToken: String, accessTokenSecret: String): Either[String, Token]

}
