import ReleaseTransformations.*

ThisBuild / organization := "io.github.takapi327"
ThisBuild / scalaVersion := "3.5.2"

lazy val releaseSettings = Seq(
  releaseProcess := Seq[ReleaseStep](
    checkSnapshotDependencies,
    inquireVersions,
    runClean,
    setReleaseVersion,
    commitReleaseVersion,
    tagRelease,
    setNextVersion,
    commitNextVersion,
    pushChanges
  )
)

lazy val helloWorld = (project in file("functions/hello-world"))
  .settings(name := "hello-world")
  .settings(
    git.gitTagToVersionNumber := { tag =>
      if (tag matches """^HelloWorld@(\d+\.\d+\.\d+)$""") Some(tag)
      else None
    }
  )
  .settings(releaseSettings)
  .enablePlugins(GitVersioning)

lazy val fizzBuzz = (project in file("functions/fizz-buzz"))
  .settings(name := "fizz-buzz")
  .settings(
    git.gitTagToVersionNumber := { tag =>
      if (tag matches """^FizzBuzz@(\d+\.\d+\.\d+)$""") Some(tag)
      else None
    }
  )
  .settings(releaseSettings)
  .enablePlugins(GitVersioning)

lazy val root = project
  .in(file("."))
  .settings(name := "feral-sandbox")
  .settings(version := "0.1.0")
  .aggregate(helloWorld, fizzBuzz)
