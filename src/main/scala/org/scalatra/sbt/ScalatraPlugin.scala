package org.scalatra.sbt

import sbt.*
import Keys.*
import java.net.URI
import com.earldouglas.xwp.ContainerPlugin.autoImport.containerPort
import com.earldouglas.xwp.JettyPlugin
import com.earldouglas.xwp.JettyPlugin.projectSettings as jettySettings
import com.earldouglas.xwp.JettyPlugin.autoImport.Jetty

object ScalatraPlugin extends AutoPlugin {
  override def requires = JettyPlugin

  val autoImport = PluginKeys

  import autoImport.*

  val browseTask = browse := {
    val log = streams.value.log

    // read port for jetty, default to 8080
    val port = {
      val p = (Jetty / containerPort).value
      if (p == -1) 8080 else p
    }

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

  val scalatraSettings: Seq[Def.Setting[?]] = jettySettings ++ Seq(browseTask)

  val scalatraWithDist: Seq[Def.Setting[?]] =
    scalatraSettings ++ DistPlugin.distSettings

  override lazy val projectSettings = scalatraSettings
}
