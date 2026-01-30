package org.scalatra.sbt

import java.util.regex.Pattern
import java.io.File
import java.util.zip.ZipFile

import scala.sys.process.*

import _root_.sbt.*
import Def.Initialize
import Keys.*
import Defaults.*
import com.earldouglas.xwp.WebappPlugin.autoImport.webappPrepare

object DistPlugin extends AutoPlugin {

  object DistKeys {
    val dist = taskKey[File](
      "Build a distribution, assemble the files, create a launcher and make an archive."
    )
    val stage = taskKey[Seq[File]](
      "Build a distribution, assemble the files and create a launcher."
    )
    val assembleJarsAndClasses = taskKey[Seq[File]]("Assemble jars and classes")
    val memSetting = settingKey[String]("The maximium and initial heap size.")
    @deprecated("there is no effect. will be removed", "")
    val permGenSetting = settingKey[String]("The PermGen size.")
    val envExports = settingKey[Seq[String]](
      "The exports which will be expored in the launcher script."
    )
    val runScriptName = settingKey[String]("The name of the service runscript.")
  }

  val autoImport = DistKeys
  import autoImport.*

  val Dist = config("dist")

  private def assembleJarsAndClassesTask: Initialize[Task[Seq[File]]] = {
    val cp = Runtime / fullClasspath
    val excl = Dist / excludeFilter
    val tgt = Dist / target

    Def.task {
      IO.delete(tgt.value)
      val (libs, dirs) =
        cp.value.files partition isArchive
      val jars =
        libs.descendantsExcept(GlobFilter("*"), excl.value) pair Path.flat(
          tgt.value / "lib"
        )
      val classesAndResources = dirs flatMap { dir =>
        val files = dir.descendantsExcept(GlobFilter("*"), excl.value)
        files pair Path.rebase(dir, tgt.value / "lib")
      }

      (IO.copy(jars) ++ IO.copy(classesAndResources)).toSeq
    }
  }

  private def isArchive(file: File): Boolean = {
    if (!file.exists() || !file.isFile) {
      false
    } else {
      try {
        new ZipFile(file)
        true
      } catch {
        case _: Exception => false
      }
    }
  }

  private def createLauncherScriptTask(
      base: File,
      name: String,
      libFiles: Seq[File],
      mainClass: Option[String],
      javaOptions: Seq[String],
      envExports: Seq[String],
      logger: Logger
  ): File = {
    val f = base / "bin" / name
    if (!f.getParentFile.exists()) f.getParentFile.mkdirs()
    IO.write(
      f,
      createScriptString(
        base,
        name,
        libFiles,
        mainClass,
        javaOptions,
        envExports
      )
    )
    "chmod +x %s".format(f.getAbsolutePath) ! logger
    f
  }

  private def createScriptString(
      base: File,
      name: String,
      libFiles: Seq[File],
      mainClass: Option[String],
      javaOptions: Seq[String],
      envExports: Seq[String]
  ): String = {
    """#!/bin/env bash
      |
      |export CLASSPATH="lib:%s"
      |export JAVA_OPTS="%s"
      |%s
      |
      |java $JAVA_OPTS -cp $CLASSPATH %s
      |""".stripMargin.format(
      classPathString(base, libFiles),
      javaOptions.mkString(" "),
      envExports.map(e => "export %s".format(e)).mkString("\n"),
      mainClass.getOrElse("")
    )
  }

  private def classPathString(base: File, libFiles: Seq[File]) = {
    (libFiles filter isArchive map (_.relativeTo(
      base
    ))).flatten mkString java.io.File.pathSeparator
  }

  private def stageTask: Initialize[Task[Seq[File]]] = {
    val webRes = webappPrepare / target
    val excl = Dist / excludeFilter
    val libFiles = Dist / assembleJarsAndClasses
    val tgt = Dist / target
    val nm = Dist / runScriptName
    val mc = Dist / mainClass
    val jo = Dist / javaOptions
    val ee = Dist / envExports
    val s = streams

    Def.task {
      val launch = createLauncherScriptTask(
        tgt.value,
        nm.value,
        libFiles.value,
        mc.value,
        jo.value,
        ee.value,
        s.value.log
      )
      val logsDir = tgt.value / "logs"
      if (!logsDir.exists()) logsDir.mkdirs()

      s.value.log.info(
        "Adding " + webRes.value + " to dist in " + tgt.value + "/webapp"
      )
      val resourceFilesFinder =
        webRes.value.descendantsExcept(GlobFilter("*"), excl.value)
      val resourceFiles = IO.copy(
        resourceFilesFinder pair Path.rebase(webRes.value, tgt.value / "webapp")
      )

      libFiles.value ++ Seq(launch, logsDir) ++ resourceFiles
    }
  }

  private def distTask: Initialize[Task[File]] = {
    val files = Dist / stage
    val tgt = Dist / target
    val nm = Dist / name
    val ver = Dist / version

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
    def apply(expression: String): PatternFileFilter = new PatternFileFilter(
      Pattern.compile(expression)
    )
  }

  val distSettings = Seq(
    Dist / excludeFilter := {
      HiddenFileFilter || PatternFileFilter(
        ".*/WEB-INF/classes"
      ) || PatternFileFilter(".*/WEB-INF/lib")
      // could use (webappDest in webapp).value.getCanonicalPath instead of .*, but webappDest is a task and SBT settings cant depend on tasks
    },
    Dist / target := (Compile / target)(_ / "dist").value,
    Dist / assembleJarsAndClasses := assembleJarsAndClassesTask.value,
    Dist / stage := stageTask.value,
    Dist / dist := distTask.value,
    dist := (Dist / dist).value,
    Dist / name := name.value,
    Dist / runScriptName := name.value,
    Dist / mainClass := Some("ScalatraLauncher"),
    Dist / memSetting := "1g",
    Dist / envExports := Seq(),
    Dist / javaOptions ++= {
      val mem = (Dist / memSetting).value
      val rr = Seq(
        "-Xms" + mem,
        "-Xmx" + mem
      )
      rr
    }
  )

  override lazy val projectSettings = distSettings
}
