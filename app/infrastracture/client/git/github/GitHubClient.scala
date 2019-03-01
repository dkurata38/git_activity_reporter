package infrastracture.client.git.github

import java.time.{LocalDate, LocalDateTime, ZoneId}

import application.client.GitClient
import domain.model.git.account.GitClientId.GitHub
import domain.model.git.account.{AccessToken, GitAccount}
import domain.model.git.event.GitEventType.Push
import domain.model.git.event.{GitEvent, GitEvents, GitRepository, GitRepositoryId}
import javax.inject.Singleton
import org.eclipse.egit.github.core.client
import org.eclipse.egit.github.core.client.PageIterator
import org.eclipse.egit.github.core.event.Event
import org.eclipse.egit.github.core.service.{EventService, UserService}

import scala.annotation.tailrec
import scala.collection.JavaConverters.collectionAsScalaIterableConverter

@Singleton
class GitHubClient extends GitClient {
  override def getUserEvents(gitAccount: GitAccount): GitEvents = {
    val eventClient = new client.GitHubClient()

    eventClient.setOAuth2Token(gitAccount.accessToken.value)
    val eventService = new EventService(eventClient)
    val eventPageIterator = eventService.pageUserEvents(gitAccount.gitUserName)

    parseEvent(eventPageIterator)
  }

  def parseEvent(eventPageIterator: PageIterator[Event]): GitEvents = {

    @tailrec
    def parseEventRecursive(eventPageIterator: PageIterator[Event], initEvents: Seq[GitEvent]): Seq[GitEvent] = {
      if (!eventPageIterator.hasNext) {
        return initEvents
      }

      val events = initEvents ++ eventPageIterator.next().asScala
          .filter(e => LocalDateTime.ofInstant(e.getCreatedAt.toInstant, ZoneId.systemDefault()).toLocalDate.isAfter(LocalDate.now().minusDays(7)))
        .filter(e => e.getType == "PushEvent")
        .map(e => new GitEvent(GitRepository(GitHub, new GitRepositoryId(e.getRepo.getName), e.getRepo.getUrl.replace("api.", "").replace("/repos", "")), Push))
      parseEventRecursive(eventPageIterator, events)
    }
    new GitEvents(parseEventRecursive(eventPageIterator, Nil))
  }

  override def getAuthenticatedUser(accessToken: AccessToken) = {
    val gitHubClient = new client.GitHubClient(accessToken.value)
    val userService = new UserService(gitHubClient)
    val user = userService.getUser
    Option(user).map(u => new GitAccount(null, GitHub, user.getName, accessToken)).orNull
  }
}