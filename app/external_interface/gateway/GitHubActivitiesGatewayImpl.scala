package external_interface.gateway

import java.time.{LocalDate, LocalDateTime, ZoneId}

import adapter.gateway.{GitHubActivitiesGateway, GitHubUserGateway}
import domain.model.git.account.GitClientId.GitHub
import domain.model.git.account.{AccessToken, GitAccount}
import domain.model.git.activity.GitActivityType.Push
import domain.model.git.activity.{GitActivities, GitActivity, GitRepository, GitRepositoryId}
import javax.inject.{Inject, Singleton}
import org.eclipse.egit.github.core.client
import org.eclipse.egit.github.core.client.{GitHubClient, PageIterator}
import org.eclipse.egit.github.core.event.Event
import org.eclipse.egit.github.core.service.{EventService, UserService}

import scala.annotation.tailrec
import scala.collection.JavaConverters.collectionAsScalaIterableConverter

@Singleton
class GitHubActivitiesGatewayImpl @Inject()(gitHubUserGateway: GitHubUserGateway) extends GitHubActivitiesGateway with GitHubUserGateway {
  override def getUserEvents(accessToken: AccessToken, from: LocalDate, to: LocalDate): GitActivities = {
    val gitHubClient = new GitHubClient()
    gitHubClient.setOAuth2Token(accessToken.value)
    Option(new UserService(gitHubClient).getUser).map {gitUser =>
      val eventPageIterator = new EventService(gitHubClient).pageUserEvents(gitUser.getName)
      parseEventRecursive(eventPageIterator)
    }.getOrElse(GitActivities.empty())

    @tailrec
    def parseEventRecursive(eventPageIterator: PageIterator[Event], initEvents: GitActivities = GitActivities.empty()): GitActivities = {
      if (!eventPageIterator.hasNext) {
        return initEvents
      }

      val events = initEvents ++ eventPageIterator.next().asScala
        .filter(e => LocalDateTime.ofInstant(e.getCreatedAt.toInstant, ZoneId.systemDefault()).toLocalDate.isAfter(LocalDate.now().minusDays(7)))
        .filter(e => e.getType == "PushEvent")
        .map(e => new GitActivity(GitRepository(GitHub, new GitRepositoryId(e.getRepo.getName), e.getRepo.getUrl.replace("api.", "").replace("/repos", "")), Push))
        .foldLeft(initEvents)((activities, activity) => activities + activity)
      parseEventRecursive(eventPageIterator, events)
    }
  }

  override def getUser(accessToken: AccessToken) = {
    val gitHubClient = new client.GitHubClient()
    gitHubClient.setOAuth2Token(accessToken.value)
    val userService = new UserService(gitHubClient)
    val user = userService.getUser
    Option(user).map(u => new GitAccount(null, GitHub, user.getName, accessToken)).orNull
  }
}