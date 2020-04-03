package airportmatching

import cats.implicits._
import cats.effect.IO
import utils.FileUtils

object CSVSourceBridge extends SourceBridge {

  def read(documentId: String): IO[Iterator[User]] =
    FileUtils
      .readFile("src/main/resources/data/" |+| documentId)
      .map(
        _
          .drop(1)
          .map(_ split ',')
          .map({
            case Array(userId, lat, long) =>
              User(userId, Point(lat.toFloat, long.toFloat))
          })
      )
}
