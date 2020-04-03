package utils

import scala.io.Source
import cats.effect.IO

object FileUtils {

  def readFile(path: String): IO[Iterator[String]] =
    IO {
      Source
        .fromFile(path)
        .getLines
    }
}
