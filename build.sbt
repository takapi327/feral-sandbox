import ReleaseTransformations.*
import sbtrelease.*
import sbtrelease.Version.*

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
      if (tag.startsWith("HelloWorld@")) Some(tag.substring("HelloWorld@".length))
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
    publish / skip := true,
    git.gitTagToVersionNumber := { tag =>
      if (tag matches """^FizzBuzz@(\d+\.\d+\.\d+)$""") Some(tag)
      else None
    },
    git.useGitDescribe := true,
    git.gitDescribePatterns := Seq("FizzBuzz@*"),
    releaseVersionBump := Version.Bump.Minor,
    releaseVersion := { rawVersion =>
      CustomVersion(rawVersion).map { version =>
          releaseVersionBump.value match {
            case Bump.Next =>
              if (version.isSnapshot) {
                version.withoutSnapshot.unapply
              } else {
                expectedSnapshotVersionError(rawVersion)
              }
            case _ => version.withoutQualifier.unapply
          }
        }
        .getOrElse(versionFormatError(rawVersion))
    },
    releaseNextVersion := {
      ver => Version(ver)
        .map(version => s"FizzBuzz@${version.bump(releaseVersionBump.value).asSnapshot.unapply}")
        .getOrElse(versionFormatError(ver))
    },
  )
  //.settings(releaseSettings)
  .enablePlugins(GitVersioning)

lazy val root = project
  .in(file("."))
  .settings(name := "feral-sandbox")
  .settings(publish / skip := true)
  .aggregate(helloWorld, fizzBuzz)
