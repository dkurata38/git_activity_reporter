package external_interface.gateway.github

import java.time.{LocalDate, LocalDateTime, ZoneId}

import adapter.gateway.github.{GitHubActivitiesGateway, GitHubUserGateway}
import domain.git.GitClientId.GitHub
import domain.git.account.{AccessToken, GitAccount}
import domain.git.activity.{GitRepository, GitRepositoryId, PushActivities, PushActivity}
import javax.inject.{Inject, Singleton}
import org.eclipse.egit.github.core.client
import org.eclipse.egit.github.core.client.{GitHubClient, PageIterator}
import org.eclipse.egit.github.core.event.{Event, PushPayload}
import org.eclipse.egit.github.core.service.{EventService, UserService}

import scala.annotation.tailrec
import scala.collection.JavaConverters.collectionAsScalaIterableConverter

@Singleton
class GitHubActivitiesGatewayImpl @Inject() extends GitHubActivitiesGateway with GitHubUserGateway {
  override def getUserEvents(accessToken: AccessToken, from: LocalDate, to: LocalDate): PushActivities = {
    val gitHubClient = new GitHubClient()
    gitHubClient.setOAuth2Token(accessToken.value)

    @tailrec
    def parseEventRecursive(eventPageIterator: PageIterator[Event], initEvents: PushActivities = PushActivities.empty()): PushActivities = {
      if (!eventPageIterator.hasNext) {
        return initEvents
      }
      val events = initEvents ++ eventPageIterator.next().asScala
        .filter(e => LocalDateTime.ofInstant(e.getCreatedAt.toInstant, ZoneId.systemDefault()).toLocalDate.isAfter(from))
        .filter(e => e.getType == "PushEvent")
        .map(e => e.toPushActivity)
        .foldLeft(initEvents)((activities, activity) => if (activity == null) activities else activities + activity)
      parseEventRecursive(eventPageIterator, events)
    }

    Option(new UserService(gitHubClient).getUser).map {gitUser =>
      val eventPageIterator = new EventService(gitHubClient).pageUserEvents(gitUser.getLogin)
      parseEventRecursive(eventPageIterator)
    }.getOrElse(PushActivities.empty())
  }

  override def getUser(accessToken: AccessToken): Option[GitAccount] = {
    val gitHubClient = new client.GitHubClient()
    gitHubClient.setOAuth2Token(accessToken.value)
    val userService = new UserService(gitHubClient)
    val user = userService.getUser
    Option(user).map(u => new GitAccount(null, GitHub, u.getLogin, accessToken))
  }

  implicit class EventsConverter(event: Event) {
    def toPushActivity : PushActivity = {
        new PushActivity(
          GitRepository(
            GitHub,
            new GitRepositoryId(event.getRepo.getName),
            event.getRepo.getUrl.replace("api.", "").replace("/repos", "")
          ),
          event.getPayload.asInstanceOf[PushPayload].getCommits.asScala.map(commit => commit.getSha).toSeq
        )
      }
  }
}
