package airportmatching

import cats.effect.IO
import utils.FileUtils

object DataLoader {

  private val AirportsFile = "src/main/resources/data/optd-airports-sample.csv"

  def hydrateArtemis: IO[Artemis] =
    FileUtils
      .readFile(AirportsFile)
      .map(
        _
          .drop(1)
          .map(row => {
            val Array(id, lat, long) = row split ','
            Airport(id, Point(lat.toFloat, long.toFloat))
          })
          .toList
      )
      .map(Artemis(_))
}
