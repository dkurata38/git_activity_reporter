package application.interactor

import java.time.LocalDate

import application.inputport.GitActivityQueryUseCaseInputPort
import application.repository.IUserTokenRepository
import domain.model.git.account.{GitAccount, GitAccountRepository}
import domain.model.git.activity.{GitActivities, GitActivitiesRepository}
import domain.model.user_token.Token
import javax.inject.{Inject, Singleton}

@Singleton
class GitActivityQueryUseCaseInteractor @Inject() (
                                                    private implicit val userTokenRepository: IUserTokenRepository,
                                                    private implicit val gitAccountRepository: GitAccountRepository,
                                                    private implicit val gitActivitiesRepository: GitActivitiesRepository
                                                  ) extends GitActivityQueryUseCaseInputPort{
  override def queryGitActivities(token: String): GitActivities = {
    userTokenRepository.findByUserToken(Token(token))
      .map(userToken => gitAccountRepository.findAllByUserId(userToken.userId)
        .map(gitAccount => gitAccount.queryGitActivities(LocalDate.now, LocalDate.now().minusDays(7)))
        .fold(GitActivities.empty())((l1, l2) => l1 ++ l2)
      ).getOrElse(GitActivities.empty())
  }

  implicit class GitUser(gitAccount: GitAccount) {
    def queryGitActivities(from: LocalDate, to: LocalDate)(implicit gitActivitiesRepository: GitActivitiesRepository)
    = gitActivitiesRepository.findByUserIdCreatedAtBetween(gitAccount, from, to)
  }

}

