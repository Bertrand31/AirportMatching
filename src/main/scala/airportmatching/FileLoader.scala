package airportmatching

import utils.FileUtils
import KDTree.KDNode

object FileLoader {

  def loadData: Option[KDNode[Float]] =
    FileUtils.readFile("src/main/resources/data/optd-airports-sample.csv")
      .toOption
      .flatMap(iterator => {
        val data =
          iterator
            .drop(1)
            .map(row => {
              val Array(id, lat, long) = row.split(",")
              List(lat.toFloat, long.toFloat)
            })
            .toSeq
        KDTree(data, depth=8)
      })
}
