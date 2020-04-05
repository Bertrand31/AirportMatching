package airportmatching

import cats.effect.IO
import utils.FileUtils

object DataLoader {

  private val AirportsFilePath = "src/main/resources/data/optd-airports-sample.csv"

  def hydrateArtemis: IO[Artemis] =
    FileUtils
      .readFile(AirportsFilePath)
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
