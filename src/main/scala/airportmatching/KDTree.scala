// https://rosettacode.org/wiki/K-d_tree#Scala

package airportmatching

import cats.implicits._

object KDTree {

  def apply[T](points: Seq[Seq[T]], depth: Int = 0)(implicit num: Numeric[T]): Option[KDNode[T]] = {
    val dim = points.headOption.map(_.size) getOrElse 0
    if (points.isEmpty || dim < 1) None
    else {
      val axis = depth % dim
      val sorted = points.sortBy(_(axis))
      val median = sorted(sorted.size / 2)(axis)
      val (left, right) = sorted.partition(v => num.lt(v(axis), median))
      Some(KDNode(right.head, apply(left, depth + 1), apply(right.tail, depth + 1), axis))
    }
  }

  case class KDNode[T](value: Seq[T], left: Option[KDNode[T]], right: Option[KDNode[T]], axis: Int)
                      (implicit num: Numeric[T]) {

    def nearest(to: Seq[T]): Nearest[T] = {
      val default = Nearest(value, to)
      compare(to, value) match {
        case 0 => default // exact match
        case t =>
          lazy val bestL = left.map(_ nearest to).getOrElse(default)
          lazy val bestR = right.map(_ nearest to).getOrElse(default)
          val branch1 = if (t < 0) bestL else bestR
          if (num.lt(branch1.distsq, default.distsq)) branch1 else default
      }
    }
  }

  case class Nearest[T](value: Seq[T], to: Seq[T])(implicit num: Numeric[T]) {
    lazy val distsq = KDTree.distsq(value, to)
  }

  // Numeric utilities
  private def distsq[T](a: Seq[T], b: Seq[T])(implicit num: Numeric[T]): T =
    (a zip b)
      .map(c => num.times(num.minus(c._1, c._2), num.minus(c._1, c._2)))
      .sum

  private def compare[T](a: Seq[T], b: Seq[T])(implicit num: Numeric[T]): Int =
    (a zip b)
      .map(c => num.compare(c._1, c._2))
      .find(_ =!= 0)
      .getOrElse(0)
}
