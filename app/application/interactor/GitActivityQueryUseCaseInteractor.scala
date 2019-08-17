package application.interactor

import java.time.LocalDate

import application.inputport.{GitActivityQueryUseCaseInputPort, LoadPushActivityUseCaseInputPort}
import domain.git.account.{GitAccount, GitAccountRepository}
import domain.git.activity.{GitActivitiesRepository, PushActivities}
import domain.user.{Token, UserTokenRepository}
import javax.inject.{Inject, Singleton}

@Singleton
class GitActivityQueryUseCaseInteractor @Inject() (
                                                    private implicit val userTokenRepository: UserTokenRepository,
                                                    private implicit val gitAccountRepository: GitAccountRepository,
                                                    private implicit val gitActivitiesRepository: GitActivitiesRepository
                                                  ) extends GitActivityQueryUseCaseInputPort{
  override def queryGitActivities(token: String): PushActivities = {
    userTokenRepository.findByUserToken(Token(token))
      .map(userToken => gitAccountRepository.findAllByUserId(userToken.userId)
        .map(gitAccount => gitAccount.queryGitActivities(LocalDate.now().minusDays(7), LocalDate.now))
        .fold(PushActivities.empty())((l1, l2) => l1 ++ l2)
      ).getOrElse(PushActivities.empty())
  }

  implicit class GitUser(gitAccount: GitAccount) {
    def queryGitActivities(from: LocalDate, to: LocalDate)(implicit gitActivitiesRepository: GitActivitiesRepository): PushActivities
    = gitActivitiesRepository.findByUserIdCreatedAtBetween(gitAccount, from, to)
  }

}

