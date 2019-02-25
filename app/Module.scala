import com.github.dkurata38.git_activity_reporter.application.repository.{IGitAccountRepository, ISocialAccountRepository, IUserRepository}
import com.github.dkurata38.git_activity_reporter.infrastracture.repository.UserRepository
import com.github.dkurata38.git_activity_reporter.infrastracture.repository.git_account.GitAccountRepository
import com.github.dkurata38.git_activity_reporter.infrastracture.repository.social_account.SocialAccountRepository
import com.google.inject.AbstractModule

class Module extends AbstractModule{
  override def configure = {
    bind(classOf[IUserRepository]).to(classOf[UserRepository])
    bind(classOf[ISocialAccountRepository]).to(classOf[SocialAccountRepository])
    bind(classOf[IGitAccountRepository]).to(classOf[GitAccountRepository])
  }
}
