package adapter.web.controllers

import com.typesafe.config.Config
import play.api.ConfigLoader

case class OAuthConfig(clientId: String, clientSecret: String, callbackUrl: String)

object OAuthConfig {
  implicit val configLoader: ConfigLoader[OAuthConfig] = new ConfigLoader[OAuthConfig] {
    def load(rootConfig: Config, path: String): OAuthConfig = {
      val config = rootConfig.getConfig(path)
      OAuthConfig(
        clientId = config.getString("client-id"),
        clientSecret = config.getString("client-secret"),
        callbackUrl = config.getString("callback-url")
      )
    }
  }
}
