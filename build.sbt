import sbtrelease.*

ThisBuild / organization := "io.github.takapi327"
ThisBuild / scalaVersion := "3.5.2"

def releaseSettings(prefix: String) = Seq(
  git.gitTagToVersionNumber := { tag =>
    if (tag matches s"""^$prefix@([0-9]+)((?:\\.[0-9]+)+)?([\\.\\-0-9a-zA-Z]*)?""") Some(tag)
    else None
  },
  git.useGitDescribe := true,
  git.gitDescribePatterns := Seq(s"$prefix@*"),
  releaseVersionBump := Version.Bump.Minor,
  releaseTagName := {
    val rawVersion = git.gitDescribedVersion.value.getOrElse((ThisBuild / version).value)
    CustomVersion.build(rawVersion)
      .fold(versionFormatError(rawVersion))(_.tag)
  },
  releaseVersion := { rawVersion =>
    CustomVersion.build(rawVersion)
      .fold(versionFormatError(rawVersion))(_.version)
  },
)

lazy val helloWorld = (project in file("functions/hello-world"))
  .settings(name := "hello-world")
  .settings(publish / skip := true)
  .settings(releaseSettings("HelloWorld")*)
  .enablePlugins(GitVersioning)

lazy val fizzBuzz = (project in file("functions/fizz-buzz"))
  .settings(name := "fizz-buzz")
  .settings(publish / skip := true)
  .settings(releaseSettings("FizzBuzz")*)
  .enablePlugins(GitVersioning)

lazy val root = project
  .in(file("."))
  .settings(name := "feral-sandbox")
  .settings(
    version := "0.1.0",
    publish / skip := true,
    releaseProcess := Seq.empty
  )
  .aggregate(helloWorld, fizzBuzz)
