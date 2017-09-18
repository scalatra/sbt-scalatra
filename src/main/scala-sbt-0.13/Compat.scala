package org.scalatra.sbt

import sbt._

object Compat {

  val ClasspathUtilities = classpath.ClasspathUtilities

  def createProcess: com.earldouglas.xwp.Compat.Process = {
    new com.earldouglas.xwp.Compat.Process {
      override def exitValue() = 0
      override def destroy() = {}
    }
  }

  def executeProccess(cmd: String, logger: ProcessLogger): Unit = {
    cmd ! logger
  }
}
