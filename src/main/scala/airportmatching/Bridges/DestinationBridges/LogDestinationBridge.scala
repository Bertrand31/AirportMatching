package airportmatching

import scala.util.chaining.scalaUtilChainingOps
import scala.collection.mutable.ArrayBuffer
import cats.effect.IO

object FakeDB {

  def write(batch: Seq[_]): IO[Unit] =
    IO {
      println(batch.mkString("\n"))
    }
}

class LogDestinationBridge[A](val batchSize: Int = 100) extends DestinationBridge[A] {

  private val batch = new ArrayBuffer[A](batchSize)

  def write(item: A): IO[Unit] = {
    this.batch += item
    if (this.batch.size >= batchSize)
      FakeDB.write(this.batch.toSeq).tap(_ => this.batch.clear)
    else
      IO.pure(())
  }
}
