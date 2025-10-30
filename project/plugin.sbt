addSbtPlugin("com.github.sbt" % "sbt-pgp" % "2.3.1")
addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.5.6")

scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature")

// scripted for plugin testing
libraryDependencies += {
  "org.scala-sbt" %% "scripted-plugin" % sbtVersion.value
}
