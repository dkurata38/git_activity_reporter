package application.interactor

import java.time.LocalDate

import application.inputport.GitActivityQueryUseCaseInputPort
import domain.git_account.{GitAccount, GitAccountRepository}
import domain.git_activity.{PushActivities, GitActivitiesRepository}
import domain.user_token.{Token, UserTokenRepository}
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
    def queryGitActivities(from: LocalDate, to: LocalDate)(implicit gitActivitiesRepository: GitActivitiesRepository)
    = gitActivitiesRepository.findByUserIdCreatedAtBetween(gitAccount, from, to)
  }

}

