package airportmatching

import cats.implicits._

object Main extends App {

  val destinationBridge = new LogDestinationBridge[String]

  val artemis = DataLoader.hydrateArtemis

  DataLoader.hydrateArtemis.flatMap(artemis =>
    CSVSourceBridge.read("user-geo-sample.csv").flatMap(
      _
        .map(user => {
          val nearest = artemis.nearest(user.location).value
          destinationBridge.write(s"uid: ${user.id}, nearest: ${nearest.name}")
        })
        .to(LazyList)
        .sequence
    )
  ).unsafeRunAsync({ _ => })
}
