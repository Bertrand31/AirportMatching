package airportmatching

import utils.FileUtils

object DataLoader {

  def hydrateArtemis: Option[ArtemisNode] =
    FileUtils
      .readFile("src/main/resources/data/optd-airports-sample.csv")
      .toOption
      .map(
        _
          .drop(1)
          .map(row => {
            val Array(id, lat, long) = row split ','
            ((lat.toFloat, long.toFloat), id)
          })
          .toList
      )
      .flatMap(Artemis(_, depth=8))
}
