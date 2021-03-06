import adapter.gateway.github.{GitHubActivitiesGateway, GitHubUserGateway}
import adapter.gateway.twitter.TwitterUserGateway
import application.client.PushActivityClientImpl
import application.inputport._
import application.interactor._
import application.repository._
import com.google.inject.AbstractModule
import domain.git.account.GitAccountRepository
import domain.git.activity.{GitActivitiesRepository, PushActivityClient}
import domain.social.SocialAccountRepository
import domain.user.{UserRepository, UserTokenRepository}
import external_interface.gateway.github.GitHubActivitiesGatewayImpl
import external_interface.gateway.twitter.TwitterUserGatewayImpl

class Module extends AbstractModule {
  override def configure = {
    bind(classOf[UserRepository]).to(classOf[UserRepositoryImpl])
    bind(classOf[PushActivityClient]).to(classOf[PushActivityClientImpl])
    bind(classOf[SocialAccountRepository]).to(classOf[SocialAccountRepositoryImpl])
    bind(classOf[GitAccountRepository]).to(classOf[GitAccountRepositoryImpl])
    bind(classOf[GitHubUserGateway]).to(classOf[GitHubActivitiesGatewayImpl])
    bind(classOf[TwitterUserGateway]).to(classOf[TwitterUserGatewayImpl])
    bind(classOf[GitHubActivitiesGateway]).to(classOf[GitHubActivitiesGatewayImpl])
    bind(classOf[GitActivityQueryUseCaseInputPort]).to(classOf[GitActivityQueryUseCaseInteractor])
    bind(classOf[LoadPushActivityUseCaseInputPort]).to(classOf[LoadPushActivityUseCaseInteractor])
    bind(classOf[LinkGitAccountUseCaseInputPort]).to(classOf[LinkGitAccountUseCaseInteractor])
    bind(classOf[LinkSocialAccountUseCaseInputPort]).to(classOf[LinkSocialAccountUseCaseInteractor])
    bind(classOf[FindUserByTokenUseCaseInputPort]).to(classOf[FindUserByTokenUseCaseInteractor])
    bind(classOf[UserActivationUseCaseInputPort]).to(classOf[UserActivationUseCaseInteractor])
    bind(classOf[UserSignInUseCaseInputPort]).to(classOf[UserSignInUseCaseInteractor])
    bind(classOf[UserTokenRepository]).to(classOf[UserTokenRepositoryImpl])
    bind(classOf[GitActivitiesRepository]).to(classOf[GitActivitiesRepositoryImpl])
    bind(classOf[FindUserByTokenUseCaseInputPort]).to(classOf[FindUserByTokenUseCaseInteractor])
  }
}
