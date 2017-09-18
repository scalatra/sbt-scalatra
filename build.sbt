import com.typesafe.sbt.SbtScalariform.ScalariformKeys
import scalariform.formatter.preferences._

lazy val root = (project in file(".")).settings(
  organization := "org.scalatra.sbt",
  name := "scalatra-sbt",
  sbtPlugin := true,
  version := "1.0.0",
  crossSbtVersions := Seq("0.13.16", "1.0.2"),
  scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature"),
  libraryDependencies += {
    Defaults.sbtPluginExtra(
      "com.earldouglas" % "xsbt-web-plugin" % "4.0.0",
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
)

val preferences =
  ScalariformKeys.preferences := ScalariformKeys.preferences.value
    .setPreference(DanglingCloseParenthesis, Force)
Seq(preferences)
