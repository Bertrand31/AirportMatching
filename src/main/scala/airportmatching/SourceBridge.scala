package airportmatching

import scala.util.Try
import cats.implicits._
import utils.FileUtils

trait SourceBridge {

  def read(documentId: String): Try[Iterator[User]]
}

object CSVSourceBridge extends SourceBridge {

  def read(documentId: String): Try[Iterator[User]] =
    FileUtils
      .readFile("src/main/resources/data/" |+| documentId)
      .map(
        _
          .drop(1)
          .map(_ split ',')
          .map({
            case Array(uid, lat, long) => User((uid, Point((lat.toFloat, long.toFloat))))
          })
      )
}
