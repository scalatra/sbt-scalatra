lazy val root = (project in file(".")).settings(
  organization := "org.scalatra.sbt",
  name := "scalatra-sbt",
  sbtPlugin := true,
  version := "0.4.1-SNAPSHOT",
  scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature"),
  javacOptions ++= Seq("-target", "1.6", "-source", "1.6"),
  publishTo <<= (version) { version: String =>
    if (version.trim.endsWith("SNAPSHOT")) Some(Opts.resolver.sonatypeSnapshots)
    else Some(Opts.resolver.sonatypeStaging)
  },
  publishMavenStyle := true,
  pomIncludeRepository := { x => false },
  pomExtra := <url>https://github.com/scalatra/scalatra-sbt/</url>
  <licenses>
    <license>
      <name>BSD License</name>
      <url>https://github.com/scalatra/scalatra-sbt/blob/master/LICENSE</url>
      <distribution>repo</distribution>
    </license>
  </licenses>
  <scm>
    <url>git@github.com:scalatra/scalatra-sbt.git</url>
    <connection>scm:git:git@github.com:scalatra/scalatra-sbt.git</connection>
  </scm>
  <developers>
    <developer>
      <id>dozed</id>
      <name>Stefan Ollinger</name>
      <url>https://github.com/dozed</url>
    </developer>
    <developer>
      <id>seratch</id>
      <name>Kazuhiro Sera</name>
      <url>http://git.io/sera</url>
    </developer>
  </developers>
).settings(scalariformSettings: _*)
 .settings(sonatypeSettings: _*)

addSbtPlugin("com.earldouglas" % "xsbt-web-plugin" % "1.1.0")
