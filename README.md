sbt-scalatra
============

[![Build Status](https://travis-ci.org/scalatra/sbt-scalatra.svg)](https://travis-ci.org/scalatra/sbt-scalatra)

An sbt plugins set for Scalatra application development.

Add the plugin in `project/plugins.sbt`

```scala
addSbtPlugin("org.scalatra.sbt" % "sbt-scalatra" % "1.0.4")
```

## ScalatraPlugin

This plugin adds a `browse` task, to open the current project in a browser.
It also enables `JettyPlugin` provided by `xsbt-web-plugin`,
so you can use task '`Jetty/start`' and '`Jetty/stop`'.

### usage

```scala
enablePlugins(ScalatraPlugin)
```

Execute the `browse` task from sbt shell, the browser starts up.

```
> Jetty/start
> browse
> Jetty/stop
```

## DistPlugin

DistPlugin unifies all the plugins in this project by grouping their settings.

### usage

```scala
enablePlugins(DistPlugin)
```

Execute the task as follows with the sbt shell, distribution file (zip) will be created.

```
> dist
```
