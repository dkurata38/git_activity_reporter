import Dependencies._

lazy val git_activity_reporter = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "com.github.dkurata38",
      scalaVersion := "2.12.8",
      version      := "0.1.0-SNAPSHOT",
      name         := "git_activity_reporter"
    )),
    libraryDependencies += scalaTest % Test
  ).enablePlugins(PlayScala)

resolvers += "Sonatype OSS Releases" at "https://oss.sonatype.org/service/local/repositories/releases/content"

libraryDependencies += guice
libraryDependencies += caffeine
libraryDependencies += ws
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "4.0.1" % Test
libraryDependencies += "org.eclipse.mylyn.github" % "org.eclipse.egit.github.core" % "2.1.5"
libraryDependencies += "org.twitter4j" % "twitter4j-core" % "4.0.4"
libraryDependencies += "com.google.firebase" % "firebase-admin" % "4.0.3"

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.example.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.example.binders._"
