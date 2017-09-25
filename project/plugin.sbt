addSbtPlugin("org.scalariform" % "sbt-scalariform"      % "1.8.0")
addSbtPlugin("com.jsuereth"    % "sbt-pgp"              % "1.0.0")

scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature")

// scripted for plugin testing
libraryDependencies += { "org.scala-sbt" %% "scripted-plugin" % sbtVersion.value }
