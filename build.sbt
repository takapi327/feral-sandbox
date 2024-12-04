ThisBuild / organization := "io.github.takapi327"
ThisBuild / scalaVersion := "3.5.2"

lazy val helloWorld = (project in file("functions/hello-world"))
  .settings(name := "hello-world")
  .settings(
    git.gitTagToVersionNumber := { tag =>
      if (tag matches """^HelloWorld@(\d+\.\d+\.\d+)$""") Some(tag)
      else None
    }
  )
  .enablePlugins(LambdaJSPlugin, GitVersioning)

lazy val root = project
  .in(file("."))
  .settings(name := "feral-sandbox")
  .settings(version := "0.1.0")
  .aggregate(helloWorld)
