import scalariform.formatter.preferences._

lazy val root = (project in file(".")).settings(
  organization := "org.scalatra.sbt",
  name := "sbt-scalatra",
  sbtPlugin := true,
  version := "1.0.4",
  crossSbtVersions += "0.13.18",
  scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature"),
  libraryDependencies += {
    Defaults.sbtPluginExtra(
      "com.earldouglas" % "xsbt-web-plugin" % "4.2.1",
      (sbtBinaryVersion in pluginCrossBuild).value,
      (scalaBinaryVersion in pluginCrossBuild).value
    )
  },
  publishTo := {
    if (version.value.trim.endsWith("SNAPSHOT")) Some(Opts.resolver.sonatypeSnapshots)
    else Some(Opts.resolver.sonatypeStaging)
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

scalariformPreferences := scalariformPreferences.value
  .setPreference(DanglingCloseParenthesis, Force)

enablePlugins(ScriptedPlugin)

scriptedLaunchOpts ++= Seq(
  "scalatra_version" -> "2.7.0",
  "scala_version" -> "2.12.10",
).map{ case (k, v) => s"-D$k=$v" }
