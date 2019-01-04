package com.github.dkurata38.git_activity_reporter.infrastracture.client.git_event.github

import com.github.dkurata38.git_activity_reporter.domain.git_account.GitAccount
import com.github.dkurata38.git_activity_reporter.domain.git_event.GitEvent.Push
import com.github.dkurata38.git_activity_reporter.domain.git_event.{GitEvent, GitEventClient, GitRepository}
import org.eclipse.egit.github.core.client.PageIterator
import org.eclipse.egit.github.core.event.Event
import org.eclipse.egit.github.core.service.EventService

import scala.annotation.tailrec
import scala.collection.JavaConverters.collectionAsScalaIterableConverter

class GitHubEventClient extends GitEventClient {
  override def getUserEvents(gitAccount: GitAccount): Seq[GitEvent] = {
    val eventService = new EventService()
    val eventPageIterator = eventService.pageUserEvents(gitAccount.gitUserName)

    parseEvent(eventPageIterator)
  }

  def parseEvent(eventPageIterator: PageIterator[Event]): Seq[GitEvent] = {

    @tailrec
    def parseEventRecursive(eventPageIterator: PageIterator[Event], events: Seq[GitEvent]): Seq[GitEvent] = {
      if (eventPageIterator.hasNext) {
        return events
      }

      events ++ eventPageIterator.next().asScala.map{e =>
        e.getType match {
          case "PushEvent" => new GitEvent(GitRepository(e.getRepo.getName, e.getRepo.getUrl), Push)
        }
      }
      parseEventRecursive(eventPageIterator, events)
    }
    parseEventRecursive(eventPageIterator, Nil)
  }
}