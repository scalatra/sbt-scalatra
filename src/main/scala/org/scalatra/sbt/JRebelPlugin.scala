package org.scalatra.sbt

import sbt._
import Keys._
import com.earldouglas.xwp.WebappPlugin.autoImport.webappPrepare
import io.Codec

object JRebelPlugin {

  import PluginKeys._

  private def generateJRebelXmlTask: sbt.Def.Initialize[Task[Unit]] = Def.task {
    val tgt = (resourceManaged in Compile).value
    val src = (crossTarget in Compile).value
    val tst = (crossTarget in Test).value
    val extra = (target in webappPrepare).value
    val s = streams.value

    val content =
      <application xsi:schemaLocation="http://www.zeroturnaround.com/alderaan/rebel-2_0.xsd" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns="http://www.zeroturnaround.com">
        <classpath>
          <dir name={ src.getAbsolutePath + "/classes" }/>
          <dir name={ tst.getAbsolutePath + "/test-classes" }></dir>
        </classpath>
        <web>
          <link>
            <dir name={ extra.getAbsolutePath }/>
          </link>
        </web>
      </application>.toString()

    val res = tgt / "rebel.xml"
    s.log.info("Generating %s.".format(res, content))
    IO.write(res, content, Codec.UTF8.charSet, append = false)
  }

  val jrebelSettings: Seq[Def.Setting[_]] = Seq(
    generateJRebel in Compile := generateJRebelXmlTask.value,
    generateJRebel := (generateJRebel in Compile).value,
    compile in Compile := (compile in Compile).dependsOn(generateJRebel in Compile).value
  )
}
