package org.scalatra.sbt

import sbt._
import Keys._

object PluginKeys {
  val generateJRebel = TaskKey[Unit]("generate-jrebel", "Generates a rebel.xml for a webapp")
  val browse = TaskKey[Unit]("browse", "Open a web browser to localhost on container:port")
}