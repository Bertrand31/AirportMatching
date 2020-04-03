// https://rosettacode.org/wiki/K-d_tree#Scala

package airportmatching

import cats.implicits._

object KDTree {

  def apply[N, A](points: Seq[(Seq[N], A)], depth: Int = 0)(implicit num: Numeric[N]): Option[KDNode[N, A]] = {
    val dim = points.headOption.map(_._1.size) getOrElse 0
    if (points.isEmpty || dim < 1) None
    else {
      val axis = depth % dim
      val sorted = points.sortBy(_._1(axis))
      val median = sorted(sorted.size / 2)._1(axis)
      val (left, right) = sorted.partition(v => num.lt(v._1(axis), median))
      Some(KDNode(right.head, apply(left, depth + 1), apply(right.tail, depth + 1), axis))
    }
  }

  case class KDNode[N, A](value: (Seq[N], A), left: Option[KDNode[N, A]], right: Option[KDNode[N, A]], axis: Int)
                      (implicit num: Numeric[N]) {

    def nearest(to: Seq[N]): Nearest[N, A] = {
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

  case class Nearest[N, A](value: (Seq[N], A), to: Seq[N])(implicit num: Numeric[N]) {
    lazy val distsq = KDTree.distsq(value._1, to)
  }

  // Numeric utilities
  private def distsq[N](a: Seq[N], b: Seq[N])(implicit num: Numeric[N]): N =
    (a zip b)
      .map(c => num.times(num.minus(c._1, c._2), num.minus(c._1, c._2)))
      .sum

  private def compare[N](a: Seq[N], b: Seq[N])(implicit num: Numeric[N]): Int =
    (a zip b)
      .map(c => num.compare(c._1, c._2))
      .find(_ =!= 0)
      .getOrElse(0)
}
