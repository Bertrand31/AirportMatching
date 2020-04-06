package airportmatching

import cats.effect._
import cats.implicits._

object Main extends IOApp {

  private val destinationBridge = new LogDestinationBridge[String]

  def run(args: List[String]): IO[ExitCode] =
    (
      DataLoader.hydrateArtemis,
      CSVSourceBridge.read("user-geo-sample.csv"),
    )
      .parMapN((artemis, users) =>
        users
          .map(user => {
            val nearest = artemis.nearest(user.location)
            destinationBridge.write(s"user: $user | airport: $nearest")
          })
          .toList
          .sequence
      )
      .flatten
      .as(ExitCode.Success)
}
