package org.scalatra.sbt

import sbt._
import scala.sys.process._

object Compat {

  val ClasspathUtilities = internal.inc.classpath.ClasspathUtilities

  def compileModules(m: UpdateReport): Option[Seq[sbt.ModuleReport]] = {
    m.configuration(Compile).map(_.modules)
  }

  def createProcess: com.earldouglas.xwp.Compat.Process = {
    new com.earldouglas.xwp.Compat.Process {
      override def exitValue() = 0
      override def destroy() = {}
      override def isAlive() = false
    }
  }

  def executeProccess(cmd: String, logger: ProcessLogger): Unit = {
    cmd ! logger
  }

}
