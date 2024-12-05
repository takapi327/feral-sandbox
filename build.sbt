import ReleaseTransformations.*
import sbtrelease.*
import sbtrelease.Version.*

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
  releaseTagName := releaseVersion.value(git.gitDescribedVersion.value.getOrElse((ThisBuild / version).value)),
  releaseVersion := { rawVersion =>
    CustomVersion(rawVersion).map { case (prefix, version) =>
        releaseVersionBump.value match {
          case Bump.Next =>
            if (version.isSnapshot) {
              s"$prefix@${version.bumpMinor.withoutSnapshot.unapply}"
            } else {
              expectedSnapshotVersionError(rawVersion)
            }
          case _ => s"$prefix@${version.bumpMinor.withoutQualifier.unapply}"
        }
      }
      .getOrElse(versionFormatError(rawVersion))
  },
  releaseNextVersion := {
    ver => CustomVersion(ver)
      .map {
        case (prefix, version) => s"$prefix@${version.bump(releaseVersionBump.value).asSnapshot.unapply}"
      }
      .getOrElse(versionFormatError(ver))
  },
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
  .enablePlugins(GitVersioning)

lazy val fizzBuzz = (project in file("functions/fizz-buzz"))
  .settings(name := "fizz-buzz")
  .settings(
    publish / skip := true,
    //git.gitTagToVersionNumber := { tag =>
    //  if (tag matches """^FizzBuzz@([0-9]+)((?:\.[0-9]+)+)?([\.\-0-9a-zA-Z]*)?""") Some(tag)
    //  else None
    //},
    //git.useGitDescribe := true,
    //git.gitDescribePatterns := Seq("FizzBuzz@*"),
    //releaseVersionBump := Version.Bump.Minor,
    //releaseTagName := releaseVersion.value(git.gitDescribedVersion.value.getOrElse((ThisBuild / version).value)),
    //releaseVersion := { rawVersion =>
    //  CustomVersion(rawVersion).map { case (prefix, version) =>
    //      releaseVersionBump.value match {
    //        case Bump.Next =>
    //          if (version.isSnapshot) {
    //            s"$prefix@${version.withoutSnapshot.unapply}"
    //          } else {
    //            expectedSnapshotVersionError(rawVersion)
    //          }
    //        case _ => s"$prefix@${version.withoutQualifier.unapply}"
    //      }
    //    }
    //    .getOrElse(versionFormatError(rawVersion))
    //},
    //releaseNextVersion := {
    //  ver => CustomVersion(ver)
    //    .map {
    //      case (prefix, version) => s"$prefix@${version.bump(releaseVersionBump.value).asSnapshot.unapply}"
    //    }
    //    .getOrElse(versionFormatError(ver))
    //},
  )
  .settings(releaseSettings("FizzBuzz"): _*)
  .enablePlugins(GitVersioning)

lazy val root = project
  .in(file("."))
  .settings(name := "feral-sandbox")
  .settings(publish / skip := true)
  .aggregate(helloWorld, fizzBuzz)
