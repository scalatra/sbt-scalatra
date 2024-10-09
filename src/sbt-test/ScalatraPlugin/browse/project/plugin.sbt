libraryDependencies += "org.http4s" %% "http4s-blaze-client" % "0.23.16"

sys.props.get("plugin.version") match {
  case Some(x) => addSbtPlugin("org.scalatra.sbt" % "sbt-scalatra" % x)
  case _ => sys.error("""|The system property 'plugin.version' is not defined.
                         |Specify this property using the scriptedLaunchOpts -D.""".stripMargin)
}
