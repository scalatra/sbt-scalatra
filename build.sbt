lazy val root = (project in file(".")).settings(
  organization := "org.scalatra.sbt",
  name := "scalatra-sbt",
  sbtPlugin := true,
  publishMavenStyle := false,
  version := "0.4.0-SNAPSHOT",
  scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature"),
  javacOptions ++= Seq("-target", "1.6", "-source", "1.6"),
  publishTo <<= (version) { version: String =>
     val scalasbt = "http://scalasbt.artifactoryonline.com/scalasbt/"
     val (name, url) = {
       if (version.contains("-SNAPSHOT")) ("sbt-plugin-snapshots", scalasbt + "sbt-plugin-snapshots")
       else ("sbt-plugin-releases", scalasbt + "sbt-plugin-releases")
     }
     Some(Resolver.url(name, new URL(url))(Resolver.ivyStylePatterns))
  }
).settings(scalariformSettings: _*)

addSbtPlugin("com.earldouglas" % "xsbt-web-plugin" % "1.0.0")
