package application.cache

import javax.inject.{Inject, Singleton}
import play.api.cache.SyncCacheApi

import scala.concurrent.duration.Duration

@Singleton
class CacheRepository @Inject() (private val cacheApi: SyncCacheApi) {
  def setCache(sessionKey :String, attributeKey: String, value: Any, duration: Duration = Duration.Inf) = cacheApi.set(sessionKey, getCacheAsMap(sessionKey) + ((attributeKey, value)), duration)

  def getCache[T](sessionKey: String, attributeKey: String) = getCacheAsMap(sessionKey: String).get(attributeKey).flatMap {
    case m: T => Some(m.asInstanceOf[T])
    case _ => None
  }

  def remove(sessionKey: String, attributeKey: String) = cacheApi.set(sessionKey, getCacheAsMap(sessionKey) - attributeKey)


  def getCacheAsMap(sessionKey: String): Map[String, Any] = cacheApi.get[Map[String, Any]](sessionKey).getOrElse(Map.empty)
}
