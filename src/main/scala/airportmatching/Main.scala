package airportmatching

object Main extends App {

  val destinationBridge = new LogDestinationBridge[String]

  val artemis = DataLoader.hydrateArtemis

  for {
    artemis <- DataLoader.hydrateArtemis
    dataStream <- CSVSourceBridge.read("user-geo-sample.csv").toOption
  } yield {
    dataStream.foreach(user => {
      val (uid, coordinates) = user
      val nearest = artemis.nearest(coordinates).value
      destinationBridge.write(s"uid: $uid, nearest: $nearest")
    })
  }
}
