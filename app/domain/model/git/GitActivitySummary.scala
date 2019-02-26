package domain.model.git

import domain.`type`.GitEventType

class GitActivitySummary(val gitRepository: GitRepository, val eventType: GitEventType, val count: Int) {

}
