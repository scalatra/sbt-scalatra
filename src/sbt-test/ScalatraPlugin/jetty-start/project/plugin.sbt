libraryDependencies += "org.http4s" %% "http4s-blaze-client" % "0.15.9a"

sys.props.get("plugin.version") match {
  case Some(x) => addSbtPlugin("org.scalatra.sbt" % "scalatra-sbt" % x)
  case _ => sys.error("""|The system property 'plugin.version' is not defined.
                         |Specify this property using the scriptedLaunchOpts -D.""".stripMargin)
}
