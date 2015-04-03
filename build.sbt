import Dependencies._

import scalariform.formatter.preferences._

lazy val root = (project in file("."))
  .settings(
    name := "reactive-consul",
    organization := "com.xebia",
    version := "0.1.0",
    scalaVersion := "2.11.5"
  )
  .aggregate(client, tools, example)

lazy val client = (project in file("client"))
  .settings(
    resolvers ++= Dependencies.resolutionRepos,
    libraryDependencies ++= Seq(
      sprayClient,
      sprayJson,
      akkaActor,
      specs2,
      spotifyDocker,
      retry,
      slf4j,
      logback % "test,it"
    ),
    ScalariformKeys.preferences := ScalariformKeys.preferences.value
      .setPreference(AlignSingleLineCaseStatements, true)
      .setPreference(DoubleIndentClassDeclaration, true)
      .setPreference(PreserveDanglingCloseParenthesis, true)
      .setPreference(RewriteArrowSymbols, true),
    scalaVersion := "2.11.5",
    scalacOptions in Test ++= Seq("-Yrangepos"),
    scalacOptions in IntegrationTest ++= Seq("-Yrangepos")
  )
  .configs( IntegrationTest )
  .settings( Defaults.itSettings : _*)
  .settings( scalariformSettingsWithIt : _* )

lazy val tools = (project in file("tools"))
  .aggregate(client)
  .dependsOn(client)
  .settings(
    scalaVersion := "2.11.5"
  )

lazy val example = (project in file("example"))
  .aggregate(client, tools)
  .dependsOn(client, tools)
  .settings(libraryDependencies ++= Seq(akkaActor, sprayClient, sprayRouting, sprayJson))
  .settings(
    libraryDependencies ++= Seq(akkaActor, sprayClient, sprayJson),
    scalaVersion := "2.11.5",
    scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature")
  )
  .enablePlugins(JavaAppPackaging)
  .settings(
    packageName in Docker := "reactive-consul-example",
    maintainer in Docker := "Dirk Louwers <dlouwers@xebia.com> & Marc Rooding <mrooding@xebia.com>",
    dockerExposedPorts in Docker := Seq(8080),
    dockerExposedVolumes in Docker := Seq("/opt/docker/logs")
  )

Revolver.settings