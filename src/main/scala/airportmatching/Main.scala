package airportmatching

object Main extends App {

  val destinationBridge = new LogDestinationBridge[String]

  val artemis = DataLoader.hydrateArtemis

  for {
    artemis <- DataLoader.hydrateArtemis
    dataStream <- CSVSourceBridge.read("user-geo-sample.csv").toOption
  } yield {
    dataStream.foreach(user => {
      val nearest = artemis.nearest(user.location).value
      destinationBridge.write(s"uid: ${user.id}, nearest: ${nearest.name}")
    })
  }
}
