package airportmatching

import cats.implicits._

// Based on https://rosettacode.org/wiki/K-d_tree, and heavily modified for this specific use-case
object Artemis {

  def apply(points: Seq[Airport], depth: Int = 0): Option[ArtemisNode] =
    if (points.isEmpty) None
    else {
      val axis = depth % 2
      def getWAxis(t: Point): Float = if (axis === 0) t.x else t.y
      val sorted = points.sortBy(t => getWAxis(t._1))
      val median = getWAxis(sorted(sorted.size / 2)._1)
      val (left, right) = sorted.partition(v => getWAxis(v._1) < median)
      Some(ArtemisNode(right.head, apply(left, depth + 1), apply(right.tail, depth + 1), axis))
    }

  def distSq(a: Point, b: Point): Double =
    Math.pow(a.x - b.x, 2) + Math.pow(a.y - b.y, 2)

  def compare(a: Point, b: Point): Int =
    (a.x compare b.x, a.y compare b.y) match {
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

  def nearest(to: Point): Nearest = {
    val default = Nearest(value, to)
    Artemis.compare(to, value._1) match {
      case 0 => default // exact match
      case t =>
        lazy val bestL = left.fold(default)(_ nearest to)
        lazy val bestR = right.fold(default)(_ nearest to)
        val branch1 = if (t < 0) bestL else bestR
        if (branch1.distSq < default.distSq) branch1 else default
    }
  }
}

final case class Nearest(value: Airport, private val to: Point) {
  lazy val distSq = Artemis.distSq(value._1, to)
}
