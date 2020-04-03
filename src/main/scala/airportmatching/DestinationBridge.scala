package airportmatching

import scala.util.chaining.scalaUtilChainingOps
import scala.collection.mutable.ArrayBuffer
import cats.effect.IO

trait DestinationBridge[A] {

  def write(item: A): IO[Unit]
}

class LogDestinationBridge[A](val batchSize: Int = 1000) extends DestinationBridge[A] {

  private val batch = new ArrayBuffer[A](batchSize)

  private object FakeDB {

    def write(a: IterableOnce[A]): IO[Unit] =
      IO {
        println(Iterator.from(1).zip(a).mkString(": "))
      }
  }

  def write(item: A): IO[Unit] = {
    this.batch += item
    if (this.batch.size >= batchSize)
      FakeDB.write(this.batch.toSeq).tap(_ => this.batch.clear)
    else
      IO.pure(())
  }
}
