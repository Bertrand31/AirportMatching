package airportmatching

object Main extends App {

  val destinationBridge = new DestinationBridge[String]

  val artemis = DataLoader.hydrateArtemis

  for {
    artemis <- DataLoader.hydrateArtemis
    dataStream <- SourceBridge.read("user-geo-sample.csv").toOption
  } yield {
    dataStream.foreach(user => {
      val (uid, coordinates) = user
      val nearest = artemis.nearest(coordinates).value
      destinationBridge.write(s"uid: $uid, nearest: $nearest")
    })
  }
}
