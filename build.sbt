name := "play-multi-skeleton"

lazy val commonSettings = Seq(
  organization := "com.github.tomdom",
  version := "0.1-SNAPSHOT",
  scalaVersion := "2.12.3"
) //++ reformatOnCompileSettings

lazy val server = (project in file("server"))
  .settings(commonSettings: _*)
  .settings(
    scalaJSProjects := Seq(client),
    pipelineStages in Assets := Seq(scalaJSPipeline),
    pipelineStages := Seq(digest, gzip),
    compile in Compile := ((compile in Compile) dependsOn scalaJSPipeline).value,
    resolvers += ("tomdom-mvn snapshots" at "https://github.com/tomdom/tomdom-mvn/raw/master/snapshots"),
    libraryDependencies ++= Seq(
      guice,
      "com.vmunier" %% "scalajs-scripts" % "1.1.1",
      "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test,
      "com.lihaoyi" %% "scalatags" % "0.6.7",
      "com.github.tomdom" %% "dummy-scala-backend" % "0.1-SNAPSHOT",
      "org.webjars" % "bootstrap" % "3.3.7"
    )
  )
  .enablePlugins(PlayScala)
  .dependsOn(sharedJvm)

lazy val client = (project in file("client"))
  .settings(commonSettings: _*)
  .settings(
    scalaJSUseMainModuleInitializer := true,
    libraryDependencies ++= Seq(
      "org.scala-js" %%% "scalajs-dom" % "0.9.4",
      "com.lihaoyi" %%% "scalatags" % "0.6.7"
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
onLoad in Global := (onLoad in Global).value andThen {s: State => "project server" :: s}
