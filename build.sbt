import Dependencies._

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
    libraryDependencies ++= Seq(sprayClient, sprayJson, akkaActor, specs2),
    scalaVersion := "2.11.5"
  )
  .configs( IntegrationTest )
  .settings( Defaults.itSettings : _*)
  .settings( libraryDependencies += spotifyDocker )

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

Revolver.settings