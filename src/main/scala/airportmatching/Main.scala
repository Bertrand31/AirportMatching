package airportmatching

import cats.implicits._
import cats.effect._

object Main extends IOApp {

  val destinationBridge = new LogDestinationBridge[String]

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
      )
      .as(ExitCode.Success)
}
