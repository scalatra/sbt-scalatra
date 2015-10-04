package org.scalatra.sbt

import sbt._
import Keys._
import Def.Initialize
import com.earldouglas.xwp.WebappPlugin.autoImport._
import com.earldouglas.xwp.ContainerPlugin.autoImport._
import com.earldouglas.xwp.ContainerPlugin._
import sbt.Keys.{ `package` => pkg }

object WarOverlayPlugin extends Plugin {

  object Keys {
    val overlayWars = taskKey[Seq[File]]("Import the files from referenced wars")
  }

  import Keys._

  private def overlayWarsTask: Initialize[Task[Seq[File]]] = Def.task {
    val fcp = sbt.Keys.update.value

    val tgt = (target in webappPrepare).value
    val s = streams.value

    s.log.info("overlaying wars in classpath to " + tgt)
    if (!tgt.exists()) tgt.mkdirs()
    val mods = fcp.configuration(Compile.name).map(_.modules).getOrElse(Seq.empty)
    val wars = (mods map { r =>
      r.artifacts collect {
        case (Artifact(_, "war", "war", _, _, _, _), f) => f
      }
    }).flatten.distinct

    s.log.info("wars: " + wars)
    val allFiles = wars map (IO.unzip(_, tgt, "*" - "META-INF/*" - "WEB-INF/*", preserveLastModified = false))
    s.log.info("Unzipped: " + allFiles)
    if (allFiles.nonEmpty) allFiles.reduce(_ ++ _).toSeq
    else Seq.empty
  }

  val warOverlaySettings: Seq[sbt.Setting[_]] = Seq(
    overlayWars in Compile <<= overlayWarsTask,
    overlayWars <<= (overlayWars in Compile),
    start in Container <<= (start in Container).dependsOn(overlayWars in Compile),
    pkg in Compile <<= (pkg in Compile).dependsOn(overlayWars in Compile)
  )
}