package domain.social

trait SocialMessageRepository {
  def send(socialAccount: SocialAccount, socialMessage: SocialMessage)
}
