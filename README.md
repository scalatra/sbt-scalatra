sbt-scalatra
============

[![Build Status](https://travis-ci.org/scalatra/scalatra-sbt.svg)](https://travis-ci.org/scalatra/scalatra-sbt)

An sbt plugins set for Scalatra application development.

Add the plugin in `project/plugins.sbt`

```scala
addSbtPlugin("org.scalatra.sbt" % "sbt-scalatra" % "1.0.0")
```

## ScalatraPlugin

This plugin adds a `browse` task, to open the current project in a browser.
It also enables `JettyPlugin` provided by `xsbt-web-plugin`,
so you can use task '`jetty:start`' and '`jetty:stop`'.

### usage

```scala
enablePlugins(ScalatraPlugin)
```

Execute the `browse` task from sbt shell, the browser starts up.

```
> jetty:start
> browse
> jetty:stop
```

## JRebelPlugin

JRebelPlugin generates a `rebel.xml` file for your project so you can use scalatra with jrebel.

### usage

```scala
enablePlugins(JRebelPlugin)
```

Execute the `generateJRebel` task from sbt shell, the `rebel.xml` will be created to target directory.

```
> generateJRebel
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
