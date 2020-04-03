package airportmatching

import utils.FileUtils
import KDTree.KDNode

object DataLoader {

  def hydrateKDTree: Option[KDNode] =
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
      .flatMap(KDTree(_, depth=8))
}
