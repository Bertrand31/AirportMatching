import org.scalatest.flatspec.AnyFlatSpec
import airportmatching.KDTree

class KDTreeSpec extends AnyFlatSpec {

  behavior of "KDTree"

  val data = List(
    List(1, 3),
    List(3, 2),
    List(3, 1),
    List(8, 9),
    List(8, -2),
    List(-5, 90),
    List(0, 0),
    List(-34, -4),
    List(8, 88),
  )
  val tree = KDTree(data, depth=8).get

  it should "return the nearest element correctly" in {

    assert(tree.nearest(List(3,3)).value == List(3, 2))
    assert(tree.nearest(List(-3,-5)).value == List(0, 0))
    assert(tree.nearest(List(-30,-5)).value == List(-34, -4))
  }
}
