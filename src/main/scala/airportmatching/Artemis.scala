package airportmatching

import cats.implicits._
import utils.GeoUtils

// Inspired by https://rosettacode.org/wiki/K-d_tree, and heavily improved and modified
object Artemis {

  private def build(airports: Seq[Airport], depth: Int): Option[ArtemisNode] =
    if (airports.isEmpty) None
    else {
      val axis = depth % 2 // Two dimensions
      def getAxisValue(a: Airport): Float = if (axis === 0) a.location.lat else a.location.lon
      val sorted = airports.sortBy(getAxisValue)
      val (left, median +: right) = sorted.splitAt(sorted.size / 2 - 1)
      Some(
        ArtemisNode(
          value = median,
          left  = build(left, depth + 1),
          right = build(right, depth + 1),
          axis  = axis,
        )
      )
    }

  def apply(airports: Seq[Airport]): ArtemisNode = build(airports, 0).get

  def compare(a: Point, b: Point): Int =
    (a.lat compare b.lat, a.lon compare b.lon) match {
      case (0, 0)         => 0
      case (xDiff, yDiff) => if (xDiff =!= 0) xDiff else yDiff
    }
}

final case class ArtemisNode(
  private val value: Airport,
  private val left: Option[ArtemisNode],
  private val right: Option[ArtemisNode],
  private val axis: Int,
) {

  private def nearestInternal(to: Point): Nearest = {
    val default = Nearest(value, to)
    Artemis.compare(to, value.location) match {
      case 0 => default // exact match
      case t =>
        lazy val bestL = left.fold(default)(_ nearestInternal to)
        lazy val bestR = right.fold(default)(_ nearestInternal to)
        val branch1 = if (t < 0) bestL else bestR
        if (branch1.distance < default.distance) branch1 else default
    }
  }

  def nearest: Point => Airport = (nearestInternal _) >>> (_.value)
}

final case class Nearest(value: Airport, private val to: Point) {
  lazy val distance = GeoUtils.distance(value.location, to)
}
