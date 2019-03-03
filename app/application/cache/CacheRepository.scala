package application.cache

import javax.inject.{Inject, Singleton}
import play.api.cache.SyncCacheApi

@Singleton
class CacheRepository @Inject() (private val cacheApi: SyncCacheApi) {
  def setCache(sessionKey :String, attributeKey: String, value: Any) = cacheApi.set(s"${sessionKey}-${attributeKey}", value)

  def getCache[T](sessionKey: String, attributeKey: String) = cacheApi.get[T](s"${sessionKey}-${attributeKey}")
}
