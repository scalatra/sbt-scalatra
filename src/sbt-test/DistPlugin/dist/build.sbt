organization := "org.scalatra"
name := "scalatra-sbt-dist-test"
version := "0.1.0-SNAPSHOT"
scalaVersion := "2.12.2"

fork in Test := true

val ScalatraVersion = "2.5.1"

libraryDependencies ++= Seq(
  "org.scalatra" %% "scalatra" % ScalatraVersion,
  "org.scalatra" %% "scalatra-specs2" % ScalatraVersion % "test",
  "javax.servlet" % "javax.servlet-api" % "3.1.0" % "provided"
)

enablePlugins(DistPlugin)
