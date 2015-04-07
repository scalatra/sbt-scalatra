package org.scalatra.sbt

import sbt._
import Keys._
import java.net.URI
import com.earldouglas.xwp.XwpPlugin._
import scala.io.Codec

object ScalatraPlugin extends Plugin {
  import PluginKeys._

  // TODO port is hidden in xwp config
  //  val browseTask = browse <<= (streams, port in container) map { (streams, port) =>
  //    import streams.log
  //    val url = URI.create("http://localhost:%s" format port)
  //    try {
  //      log info "Launching browser."
  //      java.awt.Desktop.getDesktop.browse(url)
  //    }
  //    catch {
  //      case _: Throwable => {
  //        log info { "Could not open browser, sorry. Open manually to %s." format url.toASCIIString }
  //      }
  //    }
  //  }

  val scalatraSettings: Seq[Def.Setting[_]] = jetty()

  val scalatraWithDist: Seq[Def.Setting[_]] = scalatraSettings ++ DistPlugin.distSettings

  val scalatraWithJRebel: Seq[Def.Setting[_]] = scalatraSettings ++ JRebelPlugin.jrebelSettings

  val scalatraWithWarOverlays: Seq[Def.Setting[_]] = scalatraSettings ++ WarOverlayPlugin.warOverlaySettings

  val scalatraFullSettings = scalatraSettings ++ JRebelPlugin.jrebelSettings ++ WarOverlayPlugin.warOverlaySettings

}
