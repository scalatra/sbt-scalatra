package org.scalatra.sbt

import sbt._
import Keys._

object PluginKeys {
  val generateJRebel = taskKey[Unit]("Generates a rebel.xml for a webapp")
  val browse = taskKey[Unit]("Open a web browser to localhost on container:port")
}