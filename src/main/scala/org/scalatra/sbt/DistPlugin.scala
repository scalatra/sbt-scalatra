package org.scalatra.sbt

import _root_.sbt._
import classpath.ClasspathUtilities
import Project.Initialize
import Keys._
import Defaults._

object DistPlugin extends Plugin {

  object DistKeys {
    val dist = TaskKey[File]("dist", "Build a distribution, assemble the files, create a launcher and make an archive.")
    val stage = TaskKey[Seq[File]]("stage", "Build a distribution, assemble the files and create a launcher.")
    val assembleJarsAndClasses = TaskKey[Seq[File]]("assemble-jars-and-classes", "Assemble jars and classes")
    val memSetting = SettingKey[String]("mem-setting", "The maximium and initial heap size.")
    val permGenSetting = SettingKey[String]("perm-gen-setting", "The PermGen size.")
    val envExports = SettingKey[Seq[String]]("env-exports", "The exports which will be expored in the launcher script.")
  }

  import DistKeys._
  val Dist = config("dist")

  private def assembleJarsAndClassesTask: Initialize[Task[Seq[File]]] =
    (fullClasspath in Runtime, excludeFilter in Dist, target in Dist) map { (cp, excl, tgt) =>
      IO.delete(tgt)
      val (libs, dirs) = cp.map(_.data).toSeq partition ClasspathUtilities.isArchive
      val jars = libs.descendantsExcept("*", excl) x flat(tgt / "lib")
      val classesAndResources = dirs flatMap { dir =>
        val files = dir.descendantsExcept("*", excl)
        files x rebase(dir, tgt / "lib")
      }

      (IO.copy(jars) ++ IO.copy(classesAndResources)).toSeq
    }


  private def createLauncherScriptTask(base: File, name: String, libFiles: Seq[File], mainClass: Option[String], javaOptions: Seq[String], envExports: Seq[String], logger: Logger): File = {
    val f = base / "bin" / name
    if (!f.getParentFile.exists()) f.getParentFile.mkdirs()
    IO.write(f, createScriptString(base, name, libFiles, mainClass, javaOptions, envExports))
    "chmod +x %s".format(f.getAbsolutePath) ! logger
    f
  }

  private def createScriptString(base: File, name: String, libFiles: Seq[File], mainClass: Option[String], javaOptions: Seq[String], envExports: Seq[String]): String = {
    """
|#!/bin/env bash
|
|export CLASSPATH="lib:%s"
|export JAVA_OPTS="%s"
|%s
|
|java $JAVA_OPTS -cp $CLASSPATH %s
|
""".stripMargin.format(classPathString(base, libFiles), javaOptions.mkString(" "), envExports.map(e => "export %s".format(e)).mkString("\n"), mainClass.getOrElse(""))
  }

  private def classPathString(base: File, libFiles: Seq[File]) = {
    (libFiles filter ClasspathUtilities.isArchive map (_.relativeTo(base))).flatten mkString java.io.File.pathSeparator
  }

  private[this] val webappResources = SettingKey[Seq[File]]("webapp-resources")

  private def stageTask: Initialize[Task[Seq[File]]] =
    (webappResources in Compile, excludeFilter in Dist, assembleJarsAndClasses in Dist, target in Dist, name in Dist, mainClass in Dist, javaOptions in Dist, envExports in Dist, streams) map { (webRes, excl, libFiles, tgt, nm, mainClass, javaOptions, envExports, s) =>
      val launch = createLauncherScriptTask(tgt, nm, libFiles, mainClass, javaOptions, envExports, s.log)
      val logsDir = tgt / "logs"
      if (!logsDir.exists()) logsDir.mkdirs()

      val resourceFiles = webRes flatMap { wr =>
        s.log.info("Adding " + wr + " to dist in " + tgt + "/webapp")
        val files = wr.descendantsExcept("*", excl)
        IO.copy(files x rebase(wr, tgt / "webapp"))
      }

      libFiles ++ Seq(launch, logsDir) ++ resourceFiles
    }

  private def distTask: Initialize[Task[File]] =
    (stage in Dist, target in Dist, name in Dist, version in Dist) map { (files, tgt, nm, ver) =>
      val zipFile = tgt / ".." / (nm + "-" + ver + ".zip")
      val paths = files x rebase(tgt, nm)
      IO.zip(paths, zipFile)
      zipFile
    }

  val distSettings = Seq(
     excludeFilter in Dist := HiddenFileFilter,
     target in Dist <<= (target in Compile)(_ / "dist"),
     assembleJarsAndClasses in Dist <<= assembleJarsAndClassesTask,
     stage in Dist <<= stageTask,
     dist in Dist <<= distTask,
     dist <<= dist in Dist,
     name in Dist <<= name,
     mainClass in Dist := Some("ScalatraLauncher"),
     memSetting in Dist := "1g",
     permGenSetting in Dist := "128m",
     envExports in Dist := Seq(),
     javaOptions in Dist <++= (memSetting in Dist, permGenSetting in Dist) map { (mem, perm) =>
      val rr = Seq(
        "-Xms" + mem,
        "-Xmx" + mem,
        "-XX:PermSize="+perm,
        "-XX:MaxPermSize="+perm)
      rr
    }
   )

}