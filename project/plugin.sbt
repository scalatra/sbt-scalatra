addSbtPlugin("com.github.sbt" % "sbt-pgp" % "2.4.0")
addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.6.2")

scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature")

// scripted for plugin testing
libraryDependencies += {
  "org.scala-sbt" %% "scripted-plugin" % sbtVersion.value
}
