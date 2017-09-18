organization := "org.scalatra"
name := "sbt-scalatra-container-start-test"
version := "0.1.0-SNAPSHOT"
scalaVersion := "2.12.3"

fork in Test := true

val ScalatraVersion = "2.5.1"

libraryDependencies ++= Seq(
  "org.scalatra" %% "scalatra" % ScalatraVersion,
  "org.scalatra" %% "scalatra-specs2" % ScalatraVersion % "test",
  "javax.servlet" % "javax.servlet-api" % "3.1.0" % "provided"
)

enablePlugins(ScalatraPlugin)

containerPort in Jetty := 8090

lazy val check = taskKey[Unit]("check if / is available")


check := {

  import org.http4s._

  val client = org.http4s.client.blaze.defaultClient
  val uri = Uri.uri("http://localhost:8090/")

  client.expect[String](uri).unsafePerformSyncAttempt.fold(
    error => sys.error("unexpected result: " + error),
    res => if (res != "hey") sys.error("unexpected output: " + res) else ()
  )

}
