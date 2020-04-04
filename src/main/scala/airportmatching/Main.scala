package airportmatching

import cats.effect._
import cats.implicits._

object Main extends IOApp {

  private val destinationBridge = new LogDestinationBridge[String]

  def run(args: List[String]): IO[ExitCode] =
    DataLoader.hydrateArtemis
      .flatMap(artemis =>
        CSVSourceBridge.read("user-geo-sample.csv").flatMap(
          _
            .map(user => {
              val nearest = artemis.nearest(user.location)
              destinationBridge.write(s"user: $user | airport: $nearest")
            })
            .toList
            .sequence
        )
      ).as(ExitCode.Success)
}
