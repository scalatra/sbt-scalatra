package org.scalatra.sbt

import sbt._
import Keys._
import sbt.Project.Initialize
import com.github.siasia.PluginKeys._
import io.Codec

object JRebelPlugin {

  import PluginKeys._

  def generateJRebelXmlTask: sbt.Project.Initialize[Task[Unit]] =
    (resourceManaged in Compile, crossTarget in Compile, crossTarget in Test, webappResources in Compile, streams) map {
      (tgt, src, tst, extra, s) =>
        val content = 
          <application
              xsi:schemaLocation="http://www.zeroturnaround.com/alderaan/rebel-2_0.xsd"
              xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
              xmlns="http://www.zeroturnaround.com"
              >
            <classpath>
              <dir name={src.getAbsolutePath + "/classes"} />
              <dir name={tst.getAbsolutePath + "/test-classes"}></dir>
            </classpath>
            <web>
              <link>
                {extra map { e => <dir name={e.getAbsolutePath} />}}
              </link>
            </web>
          </application>.toString()
        val res = tgt / "rebel.xml"
        s.log.info("Generating %s.".format(res, content))
        IO.write(res, content, Codec.UTF8, append = false)
    }

  val jrebelSettings: Seq[Project.Setting[_]] = Seq(
    generateJRebel in Compile <<= generateJRebelXmlTask,
    generateJRebel <<= generateJRebel in Compile,
    compile in Compile <<= (compile in Compile).dependsOn(generateJRebel in Compile)
  )
}