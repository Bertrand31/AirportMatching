import io.estatico.newtype.macros.newtype
import scala.language.implicitConversions

package object airportmatching {

  @newtype case class Point(v: (Float, Float)) {
    def x = v._1
    def y = v._2
  }

  @newtype case class Airport(v: (String, Point)) {
    def name = v._1
    def location = v._2
  }

  @newtype case class User(v: (String, Point)) {
    def id = v._1
    def location = v._2
  }
}
