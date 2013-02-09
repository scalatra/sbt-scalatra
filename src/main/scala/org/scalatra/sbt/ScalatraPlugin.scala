package org.scalatra.sbt

import sbt._
import Keys._
import java.net.URI
import com.github.siasia.PluginKeys.port
import com.github.siasia.WebPlugin.{container, webSettings}
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

  val scalatraWithJRebel: Seq[Project.Setting[_]] = scalatraSettings ++ JRebelPlugin.jrebelSettings

}