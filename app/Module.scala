import application.repository.{IGitAccountRepository, ISocialAccountRepository, IUserRepository}
import com.google.inject.AbstractModule
import infrastracture.repository.UserRepository
import infrastracture.repository.git_account.GitAccountRepository
import infrastracture.repository.social_account.SocialAccountRepository

class Module extends AbstractModule {
  override def configure = {
    bind(classOf[IUserRepository]).to(classOf[UserRepository])
    bind(classOf[ISocialAccountRepository]).to(classOf[SocialAccountRepository])
    bind(classOf[IGitAccountRepository]).to(classOf[GitAccountRepository])
  }
}
