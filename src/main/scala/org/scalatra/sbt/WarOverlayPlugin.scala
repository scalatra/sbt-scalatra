package org.scalatra.sbt

import sbt._
import Keys._
import Def.Initialize
import com.earldouglas.xwp.XwpPlugin._

object WarOverlayPlugin extends Plugin {

  object Keys {
    val overlayWars = taskKey[Seq[File]]("Import the files from referenced wars")
  }

  import Keys._

  private def overlayWarsTask: Initialize[Task[Seq[File]]] = Def.task {
    val fcp = sbt.Keys.update.value

    // TODO explain problem with target
    val tgt = (webappDest in Compile).value
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
    start in container <<= (start in container).dependsOn(overlayWars in Compile),
    packageWar in Compile <<= (packageWar in Compile).dependsOn(overlayWars in Compile)
  )
}