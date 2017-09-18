package org.scalatra.sbt

import java.util.regex.Pattern

import _root_.sbt._
import Def.Initialize
import Keys._
import Defaults._
import com.earldouglas.xwp.WebappPlugin.autoImport.webappPrepare

object DistPlugin extends AutoPlugin {

  object DistKeys {
    val dist = taskKey[File]("Build a distribution, assemble the files, create a launcher and make an archive.")
    val stage = taskKey[Seq[File]]("Build a distribution, assemble the files and create a launcher.")
    val assembleJarsAndClasses = taskKey[Seq[File]]("Assemble jars and classes")
    val memSetting = settingKey[String]("The maximium and initial heap size.")
    @deprecated("there is no effect. will be removed", "")
    val permGenSetting = settingKey[String]("The PermGen size.")
    val envExports = settingKey[Seq[String]]("The exports which will be expored in the launcher script.")
    val runScriptName = settingKey[String]("The name of the service runscript.")
  }

  val autoImport = DistKeys
  import autoImport._

  val Dist = config("dist")

  private def assembleJarsAndClassesTask: Initialize[Task[Seq[File]]] = {
    val cp = fullClasspath in Runtime
    val excl = excludeFilter in Dist
    val tgt = target in Dist

    Def.task {
      IO.delete(tgt.value)
      val (libs, dirs) = cp.value.files partition Compat.ClasspathUtilities.isArchive
      val jars = libs.descendantsExcept(GlobFilter("*"), excl.value) pair Path.flat(tgt.value / "lib")
      val classesAndResources = dirs flatMap { dir =>
        val files = dir.descendantsExcept(GlobFilter("*"), excl.value)
        files pair Path.rebase(dir, tgt.value / "lib")
      }

      (IO.copy(jars) ++ IO.copy(classesAndResources)).toSeq
    }
  }

  private def createLauncherScriptTask(base: File, name: String, libFiles: Seq[File], mainClass: Option[String], javaOptions: Seq[String], envExports: Seq[String], logger: Logger): File = {
    val f = base / "bin" / name
    if (!f.getParentFile.exists()) f.getParentFile.mkdirs()
    IO.write(f, createScriptString(base, name, libFiles, mainClass, javaOptions, envExports))
    Compat.executeProccess("chmod +x %s".format(f.getAbsolutePath), logger)
    f
  }

  private def createScriptString(base: File, name: String, libFiles: Seq[File], mainClass: Option[String], javaOptions: Seq[String], envExports: Seq[String]): String = {
    """#!/bin/env bash
      |
      |export CLASSPATH="lib:%s"
      |export JAVA_OPTS="%s"
      |%s
      |
      |java $JAVA_OPTS -cp $CLASSPATH %s
      |""".stripMargin.format(classPathString(base, libFiles), javaOptions.mkString(" "), envExports.map(e => "export %s".format(e)).mkString("\n"), mainClass.getOrElse(""))
  }

  private def classPathString(base: File, libFiles: Seq[File]) = {
    (libFiles filter Compat.ClasspathUtilities.isArchive map (_.relativeTo(base))).flatten mkString java.io.File.pathSeparator
  }

  private def stageTask: Initialize[Task[Seq[File]]] = {
    val webRes = target in webappPrepare
    val excl = excludeFilter in Dist
    val libFiles = assembleJarsAndClasses in Dist
    val tgt = target in Dist
    val nm = runScriptName in Dist
    val mc = mainClass in Dist
    val jo = javaOptions in Dist
    val ee = envExports in Dist
    val s = streams

    Def.task {
      val launch = createLauncherScriptTask(tgt.value, nm.value, libFiles.value, mc.value, jo.value, ee.value, s.value.log)
      val logsDir = tgt.value / "logs"
      if (!logsDir.exists()) logsDir.mkdirs()

      s.value.log.info("Adding " + webRes.value + " to dist in " + tgt.value + "/webapp")
      val resourceFilesFinder = webRes.value.descendantsExcept(GlobFilter("*"), excl.value)
      val resourceFiles = IO.copy(resourceFilesFinder pair Path.rebase(webRes.value, tgt.value / "webapp"))

      libFiles.value ++ Seq(launch, logsDir) ++ resourceFiles
    }
  }

  private def distTask: Initialize[Task[File]] = {
    val files = stage in Dist
    val tgt = target in Dist
    val nm = name in Dist
    val ver = version in Dist

    Def.task {
      val zipFile = tgt.value / ".." / (nm.value + "-" + ver.value + ".zip")
      val paths = files.value pair Path.rebase(tgt.value, nm.value)

      IO.zip(paths, zipFile)
      zipFile
    }
  }

  private class PatternFileFilter(val pattern: Pattern) extends FileFilter {
    def accept(file: File): Boolean = {
      pattern.matcher(file.getCanonicalPath).matches
    }
  }

  private object PatternFileFilter {
    def apply(expression: String): PatternFileFilter = new PatternFileFilter(Pattern.compile(expression))
  }

  val distSettings = Seq(
    excludeFilter in Dist := {
      HiddenFileFilter || PatternFileFilter(".*/WEB-INF/classes") || PatternFileFilter(".*/WEB-INF/lib")
      // could use (webappDest in webapp).value.getCanonicalPath instead of .*, but webappDest is a task and SBT settings cant depend on tasks
    },
    target in Dist := (target in Compile)(_ / "dist").value,
    assembleJarsAndClasses in Dist := assembleJarsAndClassesTask.value,
    stage in Dist := stageTask.value,
    dist in Dist := distTask.value,
    dist := (dist in Dist).value,
    name in Dist := name.value,
    runScriptName in Dist := name.value,
    mainClass in Dist := Some("ScalatraLauncher"),
    memSetting in Dist := "1g",
    envExports in Dist := Seq(),
    javaOptions in Dist ++= {
      val mem = (memSetting in Dist).value
      val rr = Seq(
        "-Xms" + mem,
        "-Xmx" + mem
      )
      rr
    }
  )

  override lazy val projectSettings = distSettings
}
