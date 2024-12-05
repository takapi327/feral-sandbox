import sbtrelease.Version

import scala.util.matching.Regex
import util.control.Exception.*

object CustomVersion {
  val pattern: Regex = """(.+)@([0-9]+)((?:\.[0-9]+)+)?([\.\-0-9a-zA-Z]*)?""".r

  def apply(str: String): Option[Version] = {
    allCatch opt {
      val pattern(_, maj, subs, qual) = str
      // parse the subversions (if any) to a Seq[Int]
      val subSeq: Seq[Int] = Option(subs) map { str =>
        // split on . and remove empty strings
        str.split('.').filterNot(_.trim.isEmpty).map(_.toInt).toSeq
      } getOrElse Nil
      Version(maj.toInt, subSeq, Option(qual).filterNot(_.isEmpty))
    }
  }
}
