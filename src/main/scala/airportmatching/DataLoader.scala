package airportmatching

import cats.effect.IO
import utils.FileUtils

object DataLoader {

  def hydrateArtemis: IO[ArtemisNode] =
    FileUtils
      .readFile("src/main/resources/data/optd-airports-sample.csv")
      .map(
        _
          .drop(1)
          .map(row => {
            val Array(id, lat, long) = row split ','
            Airport(id, Point(lat.toFloat, long.toFloat))
          })
          .toList
      )
      .map(Artemis(_, depth=8).get)
}
