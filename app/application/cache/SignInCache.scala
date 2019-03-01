package application.cache

import domain.model.git.account.{AccessToken, GitClientId}
import domain.model.social.{SocialAccessToken, SocialClientId}
import domain.model.user.User

case class SignInCache(user: User, private val gitOauthInfo: Map[GitClientId, AccessToken] = Map.empty, private val socialOauthInfo: Map[SocialClientId, SocialAccessToken] = Map.empty) {
  def addGitOauthInfo(clientId: GitClientId, accessToken: AccessToken) = {
    new SignInCache(user, gitOauthInfo + (clientId -> accessToken), socialOauthInfo)
  }

  def addSocialOauthInfo(clientId: SocialClientId, accessToken: SocialAccessToken) = {
    new SignInCache(user, gitOauthInfo, socialOauthInfo + (clientId -> accessToken))
  }

  def getGitAccessTokenByClientId(clientId: GitClientId) = gitOauthInfo.get(clientId)

  def getSocialAccessTokenByClientId(clientId: SocialClientId) = socialOauthInfo.get(clientId)

}
