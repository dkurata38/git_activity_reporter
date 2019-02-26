package domain.model.git

import domain.`type`.GitEventType

class GitEvent(val gitRepository: GitRepository, val eventType: GitEventType)