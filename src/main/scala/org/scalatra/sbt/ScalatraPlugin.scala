package org.scalatra.sbt

import sbt._
import Keys._
import java.net.URI
import com.earldouglas.xsbtwebplugin.PluginKeys.{port, auxCompile}
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
      case _: Throwable => {
        log info { "Could not open browser, sorry. Open manually to %s." format url.toASCIIString }
      }
    }
  }

  val reloader = TaskKey[Unit]("reloader", "Recompiles and reloads the webapp container when your code changes")

  val reloaderTask = reloader := {
    (copyResources in Compile).value
    (auxCompile in Compile).value
  }

  val scalatraSettings: Seq[Def.Setting[_]] = webSettings ++ Seq(
    browseTask, reloaderTask
  )

  val scalatraWithDist: Seq[Def.Setting[_]] = scalatraSettings ++ DistPlugin.distSettings

  val scalatraWithJRebel: Seq[Def.Setting[_]] = scalatraSettings ++ JRebelPlugin.jrebelSettings 

  val scalatraWithWarOverlays: Seq[Def.Setting[_]] = scalatraSettings ++ WarOverlayPlugin.warOverlaySettings

  val scalatraFullSettings = scalatraSettings ++ JRebelPlugin.jrebelSettings ++ WarOverlayPlugin.warOverlaySettings

}
