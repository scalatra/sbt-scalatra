lazy val root = (project in file(".")).settings(
  organization := "org.scalatra.sbt",
  name := "sbt-scalatra",
  sbtPlugin := true,
  version := "1.0.5-SNAPSHOT",
  scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature"),
  libraryDependencies += {
    Defaults.sbtPluginExtra(
      "com.earldouglas" % "sbt-war" % "5.2.1",
      (pluginCrossBuild / sbtBinaryVersion).value,
      (pluginCrossBuild / scalaBinaryVersion).value
    )
  },
  publishTo := {
    if (isSnapshot.value)
      None
    else
      localStaging.value
  },
  publishMavenStyle := true,
  pomIncludeRepository := { x => false },
  pomExtra := <url>https://github.com/scalatra/sbt-scalatra/</url>
  <licenses>
    <license>
      <name>BSD License</name>
      <url>https://github.com/scalatra/sbt-scalatra/blob/master/LICENSE</url>
      <distribution>repo</distribution>
    </license>
  </licenses>
  <scm>
    <url>git@github.com:scalatra/sbt-scalatra.git</url>
    <connection>scm:git:git@github.com:scalatra/sbt-scalatra.git</connection>
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
)

enablePlugins(ScriptedPlugin)

scriptedLaunchOpts ++= Seq(
  "scalatra_version" -> "3.2.0",
  "scala_version" -> "2.13.18"
).map { case (k, v) => s"-D$k=$v" }
