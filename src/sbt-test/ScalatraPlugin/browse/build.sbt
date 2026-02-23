organization := "org.scalatra"
name := "sbt-scalatra-browse-test"
version := "0.1.0-SNAPSHOT"
scalaVersion := sys.props("scala_version")
scalacOptions ++= {
  scalaBinaryVersion.value match {
    case "3" =>
      Nil
    case _ =>
      Seq("-Xsource:3")
  }
}

Test / fork := true

val ScalatraVersion = sys.props("scalatra_version")

libraryDependencies ++= Seq(
  "org.scalatra" %% "scalatra-javax" % ScalatraVersion,
  "org.scalatra" %% "scalatra-specs2-javax" % ScalatraVersion % "test",
)

enablePlugins(ScalatraPlugin)

Jetty / containerPort := 8090

lazy val check = taskKey[Unit]("check if / is available")

check := {
  import java.net.http.{HttpClient, HttpRequest, HttpResponse}
  import java.nio.charset.StandardCharsets

  val res = HttpClient
    .newHttpClient()
    .send(
      HttpRequest.newBuilder(new URI("http://localhost:8090")).build(),
      HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8)
    )
    .body()

  if (res != "hey") sys.error("unexpected output: " + res) else ()
}
