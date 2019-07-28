organization := "org.scalatra"
name := "sbt-scalatra-dist-test"
version := "0.1.0-SNAPSHOT"
scalaVersion := sys.props("scala_version")

fork in Test := true

val ScalatraVersion = sys.props("scalatra_version")

libraryDependencies ++= Seq(
  "org.scalatra" %% "scalatra" % ScalatraVersion,
  "org.scalatra" %% "scalatra-specs2" % ScalatraVersion % "test",
  "javax.servlet" % "javax.servlet-api" % "3.1.0" % "provided"
)

enablePlugins(DistPlugin)
