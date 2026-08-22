sbt-scalatra
============

[![CI](https://github.com/scalatra/sbt-scalatra/actions/workflows/scala.yml/badge.svg)](https://github.com/scalatra/sbt-scalatra/actions/workflows/scala.yml)

An sbt plugins set for Scalatra application development.

Add the plugin in `project/plugins.sbt`

```scala
addSbtPlugin("org.scalatra.sbt" % "sbt-scalatra" % "1.0.4")
```

## ScalatraPlugin

This plugin adds a `browse` task, to open the current project in a browser.
It also enables `SbtWar` provided by `sbt-war`,
so you can use the `warStart` and `warStop` tasks.

### usage

```scala
enablePlugins(ScalatraPlugin)
```

Execute the `browse` task from sbt shell, the browser starts up.

```
> warStart
> browse
> warStop
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
