package com.github.dkurata38.git_activity_reporter.usecase.git_activity_summary

import com.github.dkurata38.git_activity_reporter.domain.git_event.GitEvent.EventType
import com.github.dkurata38.git_activity_reporter.domain.git_event.GitRepository

class GitActivitySummary(val gitRepository: GitRepository, val eventType: EventType, val count: Int) {

}
