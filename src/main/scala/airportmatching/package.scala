import io.estatico.newtype.macros.newtype
import scala.language.implicitConversions

package object airportmatching {

  @newtype case class Point(v: (Float, Float)) {
    def x = v._1
    def y = v._2
  }

  type Airport = (Point, String)
}
