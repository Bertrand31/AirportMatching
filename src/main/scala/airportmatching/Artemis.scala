package airportmatching

import cats.implicits._
import utils.GeoUtils

// Based on https://rosettacode.org/wiki/K-d_tree, and heavily modified for our specific use-case
object Artemis {

  def apply(airports: Seq[Airport], depth: Int = 0): Option[ArtemisNode] =
    if (airports.isEmpty) None
    else {
      val axis = depth % 2 // Two dimensional tree
      def getWAxis(p: Point): Float = if (axis === 0) p.lat else p.lon
      val sorted = airports.sortBy(t => getWAxis(t.location))
      val median = getWAxis(sorted(sorted.size / 2).location)
      val (left, right) = sorted.partition(v => getWAxis(v.location) < median)
      Some(ArtemisNode(right.head, apply(left, depth + 1), apply(right.tail, depth + 1), axis))
    }

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
