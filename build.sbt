name := "play-multi-skeleton"

lazy val commonSettings = Seq(
  organization := "com.github.tomdom",
  version := "0.1-SNAPSHOT",
  scalaVersion := "2.11.8"
) //++ reformatOnCompileSettings

lazy val server = (project in file("server"))
  .settings(commonSettings: _*)
  .settings(
    scalaJSProjects := Seq(client),
    pipelineStages in Assets := Seq(scalaJSPipeline),
    pipelineStages := Seq(digest, gzip),
    // triggers scalaJSPipeline when using compile or continuous compilation
    compile in Compile <<= (compile in Compile) dependsOn scalaJSPipeline,
    resolvers += ("tomdom-mvn snapshots" at "https://github.com/tomdom/tomdom-mvn/raw/master/snapshots"),
    libraryDependencies ++= Seq(
      "com.vmunier" %% "scalajs-scripts" % "1.0.0",
      specs2 % Test,
      jdbc,
      cache,
      ws,
      "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test,
      "com.lihaoyi" %% "scalatags" % "0.6.0",
      "com.github.tomdom" %% "dummy-scala-backend" % "0.1-SNAPSHOT",
      "org.webjars" % "bootstrap" % "3.3.7"
    )
  )
  .enablePlugins(PlayScala)
  .dependsOn(sharedJvm)

lazy val client = (project in file("client"))
  .settings(commonSettings: _*)
  .settings(
    persistLauncher := true,
    persistLauncher in Test := false,
    libraryDependencies ++= Seq(
      "org.scala-js" %%% "scalajs-dom" % "0.9.1",
      "com.lihaoyi" %%% "scalatags" % "0.6.0"
    )
  )
  .enablePlugins(ScalaJSPlugin, ScalaJSWeb)
  .dependsOn(sharedJs)

lazy val shared = (crossProject.crossType(CrossType.Pure) in file("shared"))
  .settings(commonSettings: _*)
  .jsConfigure(_ enablePlugins ScalaJSWeb)

lazy val sharedJvm = shared.jvm
lazy val sharedJs = shared.js

// loads the server project at sbt startup
onLoad in Global := (Command.process("project server", _: State)) compose (onLoad in Global).value
