package domain.model.git.event

class GitEvent(val gitRepository: GitRepository, val eventType: GitEventType)