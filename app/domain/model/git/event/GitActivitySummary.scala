package domain.model.git.event

class GitActivitySummary(val gitRepository: GitRepository, val eventType: GitEventType, val count: Int) {

}
