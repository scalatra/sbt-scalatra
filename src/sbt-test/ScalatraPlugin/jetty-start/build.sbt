organization := "org.scalatra"
name := "sbt-scalatra-jetty-start-test"
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

  import org.http4s.implicits._
  import cats.effect.unsafe.implicits.global

  org.http4s.blaze.client
    .BlazeClientBuilder[cats.effect.IO]
    .resource
    .use { client =>
      val uri = uri"http://localhost:8090/"

      client
        .expect[String](uri)
        .map(res =>
          if (res != "hey") sys.error("unexpected output: " + res) else ()
        )
    }
    .unsafeRunSync()

}
