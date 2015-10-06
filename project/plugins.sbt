addSbtPlugin("com.typesafe.sbt" % "sbt-scalariform"      % "1.3.0")
addSbtPlugin("org.xerial.sbt"   % "sbt-sonatype"         % "0.2.2")

scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature")
