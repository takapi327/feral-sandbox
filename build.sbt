ThisBuild / organization := "io.github.takapi327"
ThisBuild / scalaVersion := "3.5.2"

lazy val helloWorld = (project in file("functions/hello-world"))
  .settings(name := "hello-world")
  .settings(
    git.gitTagToVersionNumber := { tag =>
      if (tag.startsWith("HelloWorld@")) Some(tag.substring("HelloWorld@".length))
      else None
    }
  )
  .enablePlugins(GitVersioning)

lazy val fizzBuzz = (project in file("functions/fizz-buzz"))
  .settings(name := "fizz-buzz")
  .settings(
    git.gitTagToVersionNumber := { tag =>
      if (tag matches """^FizzBuzz@(\d+\.\d+\.\d+)$""") Some(tag)
      else None
    }
  )
  .enablePlugins(GitVersioning)

lazy val root = project
  .in(file("."))
  .settings(name := "feral-sandbox")
  .settings(version := "0.1.0")
  .aggregate(helloWorld, fizzBuzz)
