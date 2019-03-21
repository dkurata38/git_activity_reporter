import application.repository.{GitAccountRepositoryImpl, SocialAccountRepositoryImpl, UserRepositoryImpl}
import com.google.inject.AbstractModule
import domain.git_account.GitAccountRepository
import domain.social.SocialAccountRepository
import domain.user.UserRepository

class Module extends AbstractModule {
  override def configure = {
    bind(classOf[UserRepository]).to(classOf[UserRepositoryImpl])
    bind(classOf[SocialAccountRepository]).to(classOf[SocialAccountRepositoryImpl])
    bind(classOf[GitAccountRepository]).to(classOf[GitAccountRepositoryImpl])
  }
}
