addSbtPlugin("org.scalariform" % "sbt-scalariform"      % "1.8.3")
addSbtPlugin("com.jsuereth"    % "sbt-pgp"              % "1.1.2")

scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature")

// scripted for plugin testing
libraryDependencies += { "org.scala-sbt" %% "scripted-plugin" % sbtVersion.value }
