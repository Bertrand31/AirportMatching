package airportmatching

import scala.util.Try
import scala.collection.mutable.Queue
import cats.implicits._
import utils.FileUtils

object Main extends App {

  val destinationBridge = new DestinationBridge[String]

  DataLoader.hydrateKDTree.foreach(tree => {
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

  def read(filename: String): Try[Iterator[(String, (Float, Float))]] =
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

class DestinationBridge[A](private val batch: Queue[A] = Queue.empty[A]) {

  private object FakeDB {
    def write(a: IterableOnce[A]): Unit = println(Iterator.from(1).zip(a).mkString(": "))
  }

  def write(item: A, batchSize: Int = 1000): Unit = {
    batch.enqueue(item)
    if (this.batch.size >= batchSize) {
      FakeDB.write(batch.iterator)
      batch.clear
    }
  }
}
