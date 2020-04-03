package airportmatching

import scala.util.Try
import cats.implicits._
import utils.FileUtils

object SourceBridge {

  def read(filename: String): Try[Iterator[(String, Point)]] =
    FileUtils
      .readFile("src/main/resources/data/" |+| filename)
      .map(
        _
          .drop(1)
          .map(_ split ',')
          .map({
            case Array(uid, lat, long) => (uid, (lat.toFloat, long.toFloat))
          })
      )
}
