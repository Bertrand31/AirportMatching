package airportmatching

import scala.collection.mutable.ArrayBuffer

trait DestinationBridge[A] {

  def write(item: A): Unit
}

class LogDestinationBridge[A](val batchSize: Int = 1000) extends DestinationBridge[A] {

  private val batch = new ArrayBuffer[A](batchSize)

  private object FakeDB {

    def write(a: IterableOnce[A]): Unit =
      println(Iterator.from(1).zip(a).mkString(": "))
  }

  def write(item: A): Unit = {
    this.batch += item
    if (this.batch.size >= batchSize) {
      FakeDB.write(this.batch.iterator)
      this.batch.clear
    }
  }
}
