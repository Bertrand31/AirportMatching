// https://rosettacode.org/wiki/K-d_tree#Scala

package airportmatching

import cats.implicits._

object KDTree {

  def apply[N, A](points: Seq[((N, N), A)], depth: Int = 0)(implicit num: Numeric[N]): Option[KDNode[N, A]] = {
    val dim = if (points.isEmpty) 0 else 2
    if (points.isEmpty || dim < 1) None
    else {
      val axis = depth % dim
      def getWAxis(t: (N, N)): N = if (axis === 0) t._1 else t._2
      val sorted = points.sortBy(t => getWAxis(t._1))
      val medianTuple = sorted(sorted.size / 2)._1
      val median = getWAxis(medianTuple)
      val (left, right) = sorted.partition(v => num.lt(getWAxis(v._1), median))
      Some(KDNode(right.head, apply(left, depth + 1), apply(right.tail, depth + 1), axis))
    }
  }

  case class KDNode[N, A](value: ((N, N), A), left: Option[KDNode[N, A]], right: Option[KDNode[N, A]], axis: Int)
                      (implicit num: Numeric[N]) {

    def nearest(to: (N, N)): Nearest[N, A] = {
      val default = Nearest[N, A](value, to)
      compare(to, value._1) match {
        case 0 => default // exact match
        case t =>
          lazy val bestL = left.map(_ nearest to).getOrElse(default)
          lazy val bestR = right.map(_ nearest to).getOrElse(default)
          val branch1 = if (t < 0) bestL else bestR
          if (num.lt(branch1.distsq, default.distsq)) branch1 else default
      }
    }
  }

  case class Nearest[N, A](value: ((N, N), A), to: (N, N))(implicit num: Numeric[N]) {
    lazy val distsq = KDTree.distsq(value._1, to)
  }

  // Numeric utilities
  private def distsq[N](a: (N, N), b: (N, N))(implicit num: Numeric[N]): N =
    List((a._1, b._1), (a._2, b._2))
      .map(c => num.times(num.minus(c._1, c._2), num.minus(c._1, c._2)))
      .sum

  private def compare[N](a: (N, N), b: (N, N))(implicit num: Numeric[N]): Int =
    List((a._1, b._1), (a._2, b._2))
      .map(c => num.compare(c._1, c._2))
      .find(_ =!= 0)
      .getOrElse(0)
}
