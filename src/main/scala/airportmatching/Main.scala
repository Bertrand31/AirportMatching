package airportmatching

import cats.implicits._

object Main extends App {

  val destinationBridge = new LogDestinationBridge[String]

  DataLoader.hydrateArtemis.flatMap(artemis =>
    CSVSourceBridge.read("user-geo-sample.csv").flatMap(
      _
        .map(user => {
          val nearest = artemis.nearest(user.location)
          destinationBridge.write(s"user: $user | airport: $nearest")
        })
        .to(LazyList)
        .sequence
    )
  ).unsafeRunAsync({ _ => })
}
