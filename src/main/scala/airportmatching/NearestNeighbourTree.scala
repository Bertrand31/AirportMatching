package airportmatching.artemis

import utils.MathUtils.IntImprovements
import utils.StringUtils.StringImprovements

private object Constants {

  val LatitudeAmp            = 90 // Latitude spans from -90 (90N) to 90 (90S)
  val LongitudeAmp           = 180 // Longitude spans from -180 (180W) to 180 (180E)
  val MaxCoordinateValue     = (LatitudeAmp max LongitudeAmp) * 2
  val DefaultSearchPrecision = 6

  // The lower the breadth, the deeper the tree and thus, the more precision levels available.
  // Whatever breadth we use, that will be the base in which we encode the coordinates.
  val TreeBreadth = 10
  val GPSDecimals = 6
}

sealed trait Artemis[+A]

final case class ArtemisNode[A](
  private val children: Array[Artemis[A]] = new Array[Artemis[A]](10)
) extends Artemis[A] {

  import Constants._

  type Coordinates = (Int, Int)

  private val PathDepth: Int = {
    val maxCoordinateValue = MaxCoordinateValue * Math.pow(10, GPSDecimals).toInt
    maxCoordinateValue.toBase(TreeBreadth).length
  }

  // Returns an `TreeDepth`-characters-long base-`TreeBreadth` number as a String
  private def toPaddedBase(base: Int, number: Float): String =
    Math.round(number * Math.pow(10, GPSDecimals).toInt)
      .toBase(base)
      // We need to pad the numbers to ensure all the paths have the same length so that all of the
      // trie's leaves are on the same level: at the edges of the 3-simplex the GeoTrie is).
      .padLeft(PathDepth, '0') // This method comes from StringUtils

  private def makePath(amplitude: Int, coordinate: Float): Array[Int] =
    toPaddedBase(TreeBreadth, coordinate + amplitude)
      .toCharArray
      .map(_.toInt - 48)

  // Make a GeoTrie path based on a set of coordinates
  private def latLongPath(coordinates: (Int, Int)): Array[Coordinates] =
    makePath(LatitudeAmp, coordinates._1) zip makePath(LongitudeAmp, coordinates._2)

  def +(item: A, coordinates: Coordinates): ArtemisNode[A] = ???
}

final case class ArtemisLeaf[A](private val value: A) extends Artemis[A]

