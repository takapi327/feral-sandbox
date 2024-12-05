import sbtrelease.Version

import scala.util.matching.Regex
import util.control.Exception.*

case class CustomVersion(
  tag: String,
  version: String,
)

object CustomVersion {
  val pattern: Regex = """(.+)@([0-9]+)((?:\.[0-9]+)+)?([\.\-0-9a-zA-Z]*)?""".r

  def apply(str: String): Option[(String, Version)] = {
    allCatch opt {
      val pattern(prefix, maj, subs, qual) = str
      // parse the subversions (if any) to a Seq[Int]
      val subSeq: Seq[Int] = Option(subs) map { str =>
        // split on . and remove empty strings
        str.split('.').filterNot(_.trim.isEmpty).map(_.toInt).toSeq
      } getOrElse Nil
      (prefix, Version(maj.toInt, subSeq, Option(qual).filterNot(_.isEmpty)))
    }
  }

  def build(str: String): Option[CustomVersion] = {
    allCatch opt {
      val pattern(prefix, maj, subs, qual) = str
      // parse the subversions (if any) to a Seq[Int]
      val subSeq: Seq[Int] = Option(subs) map { str =>
        // split on . and remove empty strings
        str.split('.').filterNot(_.trim.isEmpty).map(_.toInt).toSeq
      } getOrElse Nil
      val version = Version(maj.toInt, subSeq, Option(qual).filterNot(_.isEmpty))
      val newVersion = version.bumpMinor.withoutQualifier.unapply
      CustomVersion(s"$prefix@$newVersion", newVersion)
    }
  }
}
