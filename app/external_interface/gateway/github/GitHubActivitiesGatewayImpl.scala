package external_interface.gateway.github

import java.time.{LocalDate, LocalDateTime, ZoneId}

import adapter.gateway.github.{GitHubActivitiesGateway, GitHubUserGateway}
import domain.git_account.GitClientId.GitHub
import domain.git_account.{AccessToken, GitAccount}
import domain.git_activity.GitActivityType.Push
import domain.git_activity.{GitActivities, GitActivity, GitRepository, GitRepositoryId}
import javax.inject.{Inject, Singleton}
import org.eclipse.egit.github.core.client
import org.eclipse.egit.github.core.client.{GitHubClient, PageIterator}
import org.eclipse.egit.github.core.event.Event
import org.eclipse.egit.github.core.service.{EventService, UserService}

import scala.annotation.tailrec
import scala.collection.JavaConverters.collectionAsScalaIterableConverter

@Singleton
class GitHubActivitiesGatewayImpl @Inject() extends GitHubActivitiesGateway with GitHubUserGateway {
  override def getUserEvents(accessToken: AccessToken, from: LocalDate, to: LocalDate): GitActivities = {
    val gitHubClient = new GitHubClient()
    gitHubClient.setOAuth2Token(accessToken.value)

    @tailrec
    def parseEventRecursive(eventPageIterator: PageIterator[Event], initEvents: GitActivities = GitActivities.empty()): GitActivities = {
      if (!eventPageIterator.hasNext) {
        return initEvents
      }
      val events = initEvents ++ eventPageIterator.next().asScala
        .filter(e => LocalDateTime.ofInstant(e.getCreatedAt.toInstant, ZoneId.systemDefault()).toLocalDate.isAfter(from))
        .filter(e => e.getType == "PushEvent")
        .map(e => new GitActivity(GitRepository(GitHub, new GitRepositoryId(e.getRepo.getName), e.getRepo.getUrl.replace("api.", "").replace("/repos", "")), Push))
        .foldLeft(initEvents)((activities, activity) => activities + activity)
      parseEventRecursive(eventPageIterator, events)
    }

    Option(new UserService(gitHubClient).getUser).map {gitUser =>
      val eventPageIterator = new EventService(gitHubClient).pageUserEvents(gitUser.getLogin)
      parseEventRecursive(eventPageIterator)
    }.getOrElse(GitActivities.empty())
  }

  override def getUser(accessToken: AccessToken) = {
    val gitHubClient = new client.GitHubClient()
    gitHubClient.setOAuth2Token(accessToken.value)
    val userService = new UserService(gitHubClient)
    val user = userService.getUser
    Option(user).map(u => new GitAccount(null, GitHub, user.getLogin, accessToken))
  }
}