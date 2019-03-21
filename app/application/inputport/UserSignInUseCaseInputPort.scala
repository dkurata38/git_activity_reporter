package application.inputport

import domain.model.git.account.GitClientId
import domain.model.social.SocialClientId
import domain.model.user_token.Token

trait UserSignInUseCaseInputPort {
  def signInWith(clientId: GitClientId, accessToken: String): Either[String, Token]

  def signInWith(clientId: SocialClientId, accessToken: String): Either[String, Token]

}
