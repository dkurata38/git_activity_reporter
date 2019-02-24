package com.github.dkurata38.git_activity_reporter.infrastracture.client.git_event.github

import java.time.{LocalDate, LocalDateTime, ZoneId}

import com.github.dkurata38.git_activity_reporter.application.client.GitEventClient
import com.github.dkurata38.git_activity_reporter.domain.`type`.GitClientId.GitHub
import com.github.dkurata38.git_activity_reporter.domain.`type`.GitEventType.Push
import com.github.dkurata38.git_activity_reporter.domain.`type`.GitRepositoryId
import com.github.dkurata38.git_activity_reporter.domain.model.git.{GitAccount, GitEvent, GitEvents, GitRepository}
import org.eclipse.egit.github.core.client.{GitHubClient, PageIterator}
import org.eclipse.egit.github.core.event.Event
import org.eclipse.egit.github.core.service.{EventService, RepositoryService}

import scala.annotation.tailrec
import scala.collection.JavaConverters.collectionAsScalaIterableConverter

class GitHubEventClient extends GitEventClient {
  override def getUserEvents(gitAccount: GitAccount): GitEvents = {
    val eventClient = new GitHubClient()
    eventClient.setOAuth2Token(gitAccount.accessToken)
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

  def getRepositoryHtmlUrl(accessToken: String, owner: String, repositoryName: String): String = {
    val repositoryService = new RepositoryService(new GitHubClient(accessToken))
    val repository = repositoryService.getRepository(owner, repositoryName)
    repository.getHtmlUrl
  }
}