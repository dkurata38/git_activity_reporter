package domain.model.social

trait SocialMessageRepository {
  def send(socialAccount: SocialAccount, socialMessage: SocialMessage)
}
