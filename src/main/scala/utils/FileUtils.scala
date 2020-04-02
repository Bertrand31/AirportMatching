package utils

import scala.io.Source
import scala.util.Try

object FileUtils {

  def readFile(path: String): Try[Iterator[String]] =
    Try {
      Source
        .fromFile(path)
        .getLines
    }
}
