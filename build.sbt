import Dependencies._

lazy val git_activity_reporter = (project in file("."))
  .settings(
    inThisBuild(List(
      organization := "com.github.dkurata38",
      scalaVersion := "2.12.8",
      version := "0.1.0-SNAPSHOT",
      name := "git_activity_reporter"
    )),
    libraryDependencies += scalaTest % Test
  ).enablePlugins(PlayScala)

resolvers += "Sonatype OSS Releases" at "https://oss.sonatype.org/service/local/repositories/releases/content"

libraryDependencies += guice
libraryDependencies += caffeine
libraryDependencies += ws
libraryDependencies += scalaTestPlusPlay % Test
libraryDependencies += egitGithubApi
libraryDependencies += twitter4j

routesGenerator := InjectedRoutesGenerator



libraryDependencies ++= Seq(
  postgresql
  ,scalikejdbc
  ,scalikejdbcConfig
  ,scalikejdbcPlay
 )

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.example.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.example.binders._"
