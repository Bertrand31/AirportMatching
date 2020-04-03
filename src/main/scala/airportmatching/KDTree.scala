package airportmatching

import cats.implicits._

// Based on https://rosettacode.org/wiki/K-d_tree, and heavily modified for my specific use-case
object KDTree {

  type Point   = (Float, Float)
  type Airport = (Point, String)

  def apply(points: Seq[Airport], depth: Int = 0): Option[KDNode] = {
    val dim = if (points.isEmpty) 0 else 2
    if (points.isEmpty || dim < 1) None
    else {
      val axis = depth % dim
      def getWAxis(t: Point): Float = if (axis === 0) t._1 else t._2
      val sorted = points.sortBy(t => getWAxis(t._1))
      val medianTuple = sorted(sorted.size / 2)._1
      val median = getWAxis(medianTuple)
      val (left, right) = sorted.partition(v => getWAxis(v._1) < median)
      Some(KDNode(right.head, apply(left, depth + 1), apply(right.tail, depth + 1), axis))
    }
  }

  case class KDNode(value: Airport, left: Option[KDNode], right: Option[KDNode], axis: Int) {

    def nearest(to: Point): Nearest = {
      val default = Nearest(value, to)
      compare(to, value._1) match {
        case 0 => default // exact match
        case t =>
          lazy val bestL = left.map(_ nearest to).getOrElse(default)
          lazy val bestR = right.map(_ nearest to).getOrElse(default)
          val branch1 = if (t < 0) bestL else bestR
          if (branch1.distsq < default.distsq) branch1 else default
      }
    }
  }

  case class Nearest(value: Airport, to: Point) {
    lazy val distsq = KDTree.distsq(value._1, to)
  }

  // Numeric utilities
  private def distsq(a: Point, b: Point): Float =
    List((a._1, b._1), (a._2, b._2))
      .map(c => (c._1 - c._2) * (c._1 - c._2))
      .sum

  private def compare(a: Point, b: Point): Int =
    List((a._1, b._1), (a._2, b._2))
      .map(c => c._1 compare c._2)
      .find(_ =!= 0)
      .getOrElse(0)
}
