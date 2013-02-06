import sbt._
import Keys._

object ScalatraPluginBuild extends Build {

  lazy val root = Project("scalatra-sbt", file("."), settings = Defaults.defaultSettings)
}