package application.interactor

import application.inputport.LoadPushActivityUseCaseInputPort
import domain.git.account.GitAccountRepository
import domain.git.activity.{PushActivities, PushActivityClient}
import domain.user.{Token, UserTokenRepository}
import javax.inject.{Inject, Singleton}

@Singleton
class LoadPushActivityUseCaseInteractor @Inject() (
                                                    userTokenRepository: UserTokenRepository,
                                                    gitAccountRepository: GitAccountRepository,
                                                    pushActivitiesClient: PushActivityClient) extends LoadPushActivityUseCaseInputPort{
  override def queryGitActivities(token: String): Either[String, PushActivities] = {
    userTokenRepository.findByUserToken(Token(token))
      .toRight("認証エラー")
      .map(userToken => gitAccountRepository.findAllByUserId(userToken.userId))
      .map(gitAccounts => gitAccounts
        .map(pushActivitiesClient.findByUserIdCreatedAtBetween)
        .reduceLeft((accum, elem) => accum ++ elem)
      )
  }
}
