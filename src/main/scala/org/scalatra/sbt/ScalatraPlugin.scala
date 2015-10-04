package org.scalatra.sbt

import java.util.concurrent.atomic.AtomicReference

import com.earldouglas.xwp.JettyPlugin
import sbt._
import Keys._
import java.net.URI
import com.earldouglas.xwp.JettyPlugin.autoImport._
import com.earldouglas.xwp.ContainerPlugin.autoImport._

object ScalatraPlugin extends Plugin {
  import PluginKeys._

  val containerInstance = settingKey[AtomicReference[Option[Process]]]("Current container process")

  val browseTask = browse := {
    val log = streams.value.log

    // read port for jetty, default to 8080
    val port = {
      val p = (containerPort in Jetty).value
      if (p == -1) 8080 else p
    }

    val url = URI.create("http://localhost:%s" format port)
    try {
      log.info("Launching browser.")
      java.awt.Desktop.getDesktop.browse(url)
    } catch {
      case _: Throwable => {
        log.info(f"Could not open browser, sorry. Open manually to ${url.toASCIIString}")
      }
    }
  }

  val scalatraSettings: Seq[Def.Setting[_]] = JettyPlugin.projectSettings ++ Seq(browseTask)

  val scalatraWithDist: Seq[Def.Setting[_]] = scalatraSettings ++ DistPlugin.distSettings

  val scalatraWithJRebel: Seq[Def.Setting[_]] = scalatraSettings ++ JRebelPlugin.jrebelSettings

  val scalatraWithWarOverlays: Seq[Def.Setting[_]] = scalatraSettings ++ WarOverlayPlugin.warOverlaySettings

  val scalatraFullSettings = scalatraSettings ++ JRebelPlugin.jrebelSettings ++ WarOverlayPlugin.warOverlaySettings

}
