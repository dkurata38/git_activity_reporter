import sbt._

object Dependencies {
  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.0.5"
  lazy val egitGithubApi = "org.eclipse.mylyn.github" % "org.eclipse.egit.github.core" % "2.1.5"
  lazy val twitter4j = "org.twitter4j" % "twitter4j-core" % "4.0.7"
  lazy val scalaTestPlusPlay = "org.scalatestplus.play" %% "scalatestplus-play" % "4.0.1"
  lazy val postgresql = "org.postgresql" % "postgresql" % "42.2.5"
  lazy val scalikejdbc = "org.scalikejdbc" %% "scalikejdbc" % "3.3.2"
  lazy val scalikejdbcConfig = "org.scalikejdbc" %% "scalikejdbc-config" % "3.3.2"
  lazy val scalikejdbcPlay = "org.scalikejdbc" %% "scalikejdbc-play-initializer" % "2.7.0-scalikejdbc-3.3"
}
