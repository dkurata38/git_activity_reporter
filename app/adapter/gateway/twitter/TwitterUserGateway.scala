package adapter.gateway.twitter

import domain.model.social.SocialAccount

trait TwitterUserGateway {
  def getUser(accessToken: String, accessTokenSecret: String): SocialAccount
}
