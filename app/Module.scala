import application.repository.{GitAccountRepositoryImpl, ISocialAccountRepository, IUserRepository}
import com.google.inject.AbstractModule
import domain.model.git.account.GitAccountRepository
import infrastracture.repository.UserRepository
import infrastracture.repository.social_account.SocialAccountRepository

class Module extends AbstractModule {
  override def configure = {
    bind(classOf[IUserRepository]).to(classOf[UserRepository])
    bind(classOf[ISocialAccountRepository]).to(classOf[SocialAccountRepository])
    bind(classOf[GitAccountRepository]).to(classOf[GitAccountRepositoryImpl])
  }
}
