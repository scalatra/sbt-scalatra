package org.scalatra.sbt

import sbt._
import Keys._
import java.net.URI
import com.earldouglas.xsbtwebplugin.PluginKeys.port
import com.earldouglas.xsbtwebplugin.WebPlugin.{container, webSettings}
import scala.io.Codec


object ScalatraPlugin extends Plugin {
  import PluginKeys._

  
  val browseTask = browse <<= (streams, port in container.Configuration) map { (streams, port) =>
    import streams.log
    val url = URI.create("http://localhost:%s" format port)
    try {
      log info "Launching browser."
      java.awt.Desktop.getDesktop.browse(url)
    }
    catch {
      case _ => {
        log info { "Could not open browser, sorry. Open manually to %s." format url.toASCIIString }
      }
    }
  }

  val scalatraSettings: Seq[Project.Setting[_]] = webSettings ++ Seq(
    browseTask
  )

  val scalatraWithDist: Seq[Project.Setting[_]] = scalatraSettings ++ DistPlugin.distSettings

  val scalatraWithJRebel: Seq[Project.Setting[_]] = scalatraSettings ++ JRebelPlugin.jrebelSettings 

  val scalatraWithWarOverlays: Seq[Project.Setting[_]] = scalatraSettings ++ WarOverlayPlugin.warOverlaySettings

  val scalatraFullSettings = scalatraSettings ++ JRebelPlugin.jrebelSettings ++ WarOverlayPlugin.warOverlaySettings

}