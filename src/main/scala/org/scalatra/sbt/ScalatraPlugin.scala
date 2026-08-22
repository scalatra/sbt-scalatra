package org.scalatra.sbt

import sbt.*
import Keys.*
import java.net.URI
import com.earldouglas.sbt.war.SbtWar
import com.earldouglas.sbt.war.SbtWar.autoImport.warPort

object ScalatraPlugin extends AutoPlugin {
  override def requires = SbtWar

  val autoImport = PluginKeys

  import autoImport.*

  val browseTask = browse := {
    val log = streams.value.log

    val port = warPort.value

    val url = URI.create("http://localhost:%s" format port)
    try {
      log.info("Launching browser.")
      java.awt.Desktop.getDesktop.browse(url)
    } catch {
      case _: Throwable => {
        log.info(
          f"Could not open browser, sorry. Open manually to ${url.toASCIIString}"
        )
      }
    }
  }

  val scalatraSettings: Seq[Def.Setting[?]] = Seq(browseTask)

  val scalatraWithDist: Seq[Def.Setting[?]] =
    scalatraSettings ++ DistPlugin.distSettings

  override lazy val projectSettings = scalatraSettings
}
