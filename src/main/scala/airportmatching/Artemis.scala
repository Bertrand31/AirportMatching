package airportmatching

import cats.implicits._

// Based on https://rosettacode.org/wiki/K-d_tree, and heavily modified for this specific use-case
object Artemis {

  def apply(points: Seq[Airport], depth: Int = 0): Option[ArtemisNode] =
    if (points.isEmpty) None
    else {
      val axis = depth % 2
      def getWAxis(t: Point): Float = if (axis === 0) t._1 else t._2
      val sorted = points.sortBy(t => getWAxis(t._1))
      val median = getWAxis(sorted(sorted.size / 2)._1)
      val (left, right) = sorted.partition(v => getWAxis(v._1) < median)
      Some(ArtemisNode(right.head, apply(left, depth + 1), apply(right.tail, depth + 1), axis))
    }

  // Numeric utilities
  def distsq(a: Point, b: Point): Double =
    Math.pow(a._1 - b._1, 2) + Math.pow(a._2 - b._2, 2)

  def compare(a: Point, b: Point): Int =
    (a._1 compare b._1, a._2 compare b._2) match {
      case (0, 0) => 0
      case (xDiff, yDiff) => if (xDiff =!= 0) xDiff else yDiff
    }
}

final case class ArtemisNode(
  private val value: Airport,
  private val left: Option[ArtemisNode],
  private val right: Option[ArtemisNode],
  private val axis: Int,
) {

  def nearest(to: Point): Nearest = {
    val default = Nearest(value, to)
    Artemis.compare(to, value._1) match {
      case 0 => default // exact match
      case t =>
        lazy val bestL = left.map(_ nearest to).getOrElse(default)
        lazy val bestR = right.map(_ nearest to).getOrElse(default)
        val branch1 = if (t < 0) bestL else bestR
        if (branch1.distsq < default.distsq) branch1 else default
    }
  }
}

final case class Nearest(value: Airport, private val to: Point) {
  lazy val distsq = Artemis.distsq(value._1, to)
}
