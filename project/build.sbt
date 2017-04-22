addSbtPlugin("com.typesafe.sbt" % "sbt-scalariform"      % "1.3.0")

scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature")

// scripted for plugin testing
libraryDependencies += { "org.scala-sbt" % "scripted-plugin" % sbtVersion.value }
