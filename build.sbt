lazy val root = (project in file(".")).settings(
  organization := "org.scalatra.sbt",
  name := "scalatra-sbt",
  sbtPlugin := true,
  publishMavenStyle := true,
  version := "0.4.0-SNAPSHOT",
  scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature"),
  javacOptions ++= Seq("-target", "1.6", "-source", "1.6"),
  publishTo <<= (version) { version: String =>
    if (version.trim.endsWith("SNAPSHOT")) Some(Opts.resolver.sonatypeSnapshots)
    else Some(Opts.resolver.sonatypeStaging)
  }
).settings(scalariformSettings: _*)

addSbtPlugin("com.earldouglas" % "xsbt-web-plugin" % "1.0.0")
