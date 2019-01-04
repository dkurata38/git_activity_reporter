import Dependencies._

lazy val git_activity_reporter = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "com.github.dkurata38",
      scalaVersion := "2.12.7",
      version      := "0.1.0-SNAPSHOT",
      name         := "sync_git_action_log"
    )),
    libraryDependencies += scalaTest % Test
  )

resolvers += "Sonatype OSS Releases" at "https://oss.sonatype.org/service/local/repositories/releases/content"

libraryDependencies += "org.eclipse.mylyn.github" % "org.eclipse.egit.github.core" % "2.1.5"
