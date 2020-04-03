package airportmatching

import scala.collection.mutable.ArrayBuffer

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
