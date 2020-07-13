package org.scalatra.sbt

import sbt._
import Keys._

object PluginKeys {
  val browse = taskKey[Unit]("Open a web browser to localhost on container:port")
}
