package airportmatching

import cats.effect.IO

trait DestinationBridge[A] {

  def write(item: A): IO[Unit]
}
