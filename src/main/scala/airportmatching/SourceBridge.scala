package airportmatching

import scala.util.Try
import cats.implicits._
import utils.FileUtils

trait SourceBridge {

  def read(documentId: String): Try[Iterator[(String, Point)]]
}

object CSVSourceBridge extends SourceBridge {

  def read(documentId: String): Try[Iterator[(String, Point)]] =
    FileUtils
      .readFile("src/main/resources/data/" |+| documentId)
      .map(
        _
          .drop(1)
          .map(_ split ',')
          .map({
            case Array(uid, lat, long) => (uid, Point((lat.toFloat, long.toFloat)))
          })
      )
}
