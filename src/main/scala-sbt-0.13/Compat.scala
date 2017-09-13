package org.scalatra.sbt

import sbt._

object Compat {
  
  val ClasspathUtilities = classpath.ClasspathUtilities

  def compileModules(m: UpdateReport): Option[Seq[sbt.ModuleReport]] = {
     m.configuration(Compile.name).map(_.modules)
  }

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
