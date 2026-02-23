organization := "org.scalatra"
name := "sbt-scalatra-dist-test"
version := "0.1.0-SNAPSHOT"
scalaVersion := sys.props("scala_version")
scalacOptions ++= {
  scalaBinaryVersion.value match {
    case "3" =>
      Nil
    case _ =>
      Seq("-Xsource:3")
  }
}

Test / fork := true

val ScalatraVersion = sys.props("scalatra_version")

libraryDependencies ++= Seq(
  "org.scalatra" %% "scalatra-javax" % ScalatraVersion,
  "org.scalatra" %% "scalatra-specs2-javax" % ScalatraVersion % "test",
)

enablePlugins(DistPlugin)
