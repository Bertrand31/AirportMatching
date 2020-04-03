package airportmatching

import utils.FileUtils

object Main extends App {

  DataLoader.hydrateKDTree.foreach(tree => {

    FileUtils.readFile("src/main/resources/data/user-geo-sample.csv").foreach(iterator => {
      iterator.drop(1).foreach(user => {
        val Array(uid, lat, long) = user.split(",")
        val nearest = tree.nearest(List(lat.toFloat, long.toFloat)).value
        println(s"uid: $uid, nearest: $nearest")
      })
    })
  })
}
