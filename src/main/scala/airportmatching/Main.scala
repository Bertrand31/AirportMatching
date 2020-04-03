package airportmatching

import scala.util.Try
import scala.collection.mutable.ArrayBuffer
import cats.implicits._
import utils.FileUtils

object Main extends App {

  val destinationBridge = new DestinationBridge[String]

  val artemis = DataLoader.hydrateArtemis

  artemis.foreach(tree => {
    SourceBridge.read("user-geo-sample.csv").foreach(iterator => {
      iterator.foreach(user => {
        val (uid, coordinates) = user
        val nearest = tree.nearest(coordinates).value
        destinationBridge.write(s"uid: $uid, nearest: $nearest")
      })
    })
  })
}

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

class DestinationBridge[A](val batchSize: Int = 1000) {

  private val batch = new ArrayBuffer[A](batchSize)

  private object FakeDB {
    def write(a: IterableOnce[A]): Unit = println(Iterator.from(1).zip(a).mkString(": "))
  }

  def write(item: A): Unit = {
    this.batch += item
    if (this.batch.size >= batchSize) {
      FakeDB.write(batch.iterator)
      this.batch.clear
    }
  }
}
