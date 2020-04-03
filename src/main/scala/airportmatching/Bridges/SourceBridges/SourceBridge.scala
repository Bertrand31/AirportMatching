package airportmatching

import cats.effect.IO

trait SourceBridge {

  def read(documentId: String): IO[Iterator[User]]
}
