package adapter.gateway.twitter

import domain.social.SocialAccount


trait TwitterUserGateway {
  def getUser(accessToken: String, accessTokenSecret: String): Option[SocialAccount]
}
