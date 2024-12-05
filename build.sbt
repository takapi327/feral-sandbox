import ReleaseTransformations.*

ThisBuild / organization := "io.github.takapi327"
ThisBuild / scalaVersion := "3.5.2"

lazy val releaseSettings = Seq(
  releaseTagName       := (ThisBuild / version).value,
  releaseTagComment    := s"Release version ${ (ThisBuild / version).value } [ci skip]",
  releaseCommitMessage := s"Setting version to ${ (ThisBuild / version).value } [ci skip]",
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
    },
    git.useGitDescribe := true,
    git.gitDescribePatterns := Seq("HelloWorld@*")
  )
  .settings(releaseSettings)
  .enablePlugins(GitVersioning)

lazy val fizzBuzz = (project in file("functions/fizz-buzz"))
  .settings(name := "fizz-buzz")
  .settings(
    git.gitTagToVersionNumber := { tag =>
      if (tag matches """^FizzBuzz@(\d+\.\d+\.\d+)$""") Some(tag)
      else None
    },
    git.useGitDescribe := true,
    git.gitDescribePatterns := Seq("FizzBuzz@*")
  )
  .settings(releaseSettings)
  .enablePlugins(GitVersioning)

lazy val root = project
  .in(file("."))
  .settings(name := "feral-sandbox")
  .settings(publish / skip := true)
  .aggregate(helloWorld, fizzBuzz)
