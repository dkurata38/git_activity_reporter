import application.repository.{GitAccountRepositoryImpl, IUserRepository, SocialAccountRepositoryImpl}
import com.google.inject.AbstractModule
import domain.model.git.account.GitAccountRepository
import domain.model.social.SocialAccountRepository
import infrastracture.repository.UserRepository

class Module extends AbstractModule {
  override def configure = {
    bind(classOf[IUserRepository]).to(classOf[UserRepository])
    bind(classOf[SocialAccountRepository]).to(classOf[SocialAccountRepositoryImpl])
    bind(classOf[GitAccountRepository]).to(classOf[GitAccountRepositoryImpl])
  }
}
