import scala.language.implicitConversions
import io.estatico.newtype.macros.newtype
import io.estatico.newtype.ops.toCoercibleIdOps

package object airportmatching {

  @newtype case class Point(v: (Float, Float)) {
    def x = v._1
    def y = v._2
  }

  object Point {
    def apply(x: Float, y: Float): Point = (x, y).coerce
  }

  @newtype case class Airport(v: (String, Point)) {
    def name = v._1
    def location = v._2
  }

  object Airport {
    def apply(name: String, point: Point): Airport = (name, point).coerce
  }

  @newtype case class User(v: (String, Point)) {
    def id = v._1
    def location = v._2
  }

  object User {
    def apply(userId: String, point: Point): User = (userId, point).coerce
  }
}
