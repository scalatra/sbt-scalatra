scalatra-sbt
============

[![Build Status](https://travis-ci.org/scalatra/scalatra-sbt.svg)](https://travis-ci.org/scalatra/scalatra-sbt)

An sbt Plugin to for scalatra apps.

```scala
addSbtPlugin("org.scalatra.sbt" % "scalatra-sbt"    % "0.4.0")
addSbtPlugin("com.earldouglas"  % "xsbt-web-plugin" % "1.1.0")
```

This plugin adds a browse task, to open the current project in a browser. It depends on xsbt-web-plugin so you don't need to specify that explicitly.

It also has a JRebel plugin that will generate a rebel.xml file for your project so you can use scalatra with jrebel.

A third plugin is a mini war-overlay plugin. it allows you to depend on war files and it will copy the static files out of those wars and into the current project.
You can use it to depend on jquery-atmosphere for example.

And lastly there is a plugin that unifies all the plugins in this project by grouping their settings.

