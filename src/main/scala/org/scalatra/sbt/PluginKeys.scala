package org.scalatra.sbt

import sbt.*
import Keys.*

object PluginKeys {
  val browse =
    taskKey[Unit]("Open a web browser to localhost on container:port")
}
