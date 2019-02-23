package com.github.dkurata38.git_activity_reporter.domain.model.git

import com.github.dkurata38.git_activity_reporter.domain.`type`.GitEventType

class GitEvent(val gitRepository: GitRepository, val eventType: GitEventType)