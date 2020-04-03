import org.scalatest.flatspec.AnyFlatSpec
import airportmatching.KDTree

class KDTreeSpec extends AnyFlatSpec {

  behavior of "KDTree"

  val data: List[((Float, Float), String)] = List(
    ((1, 3), "foo"),
    ((3, 2), "bar"),
    ((3, 1), "baz"),
    ((8, 9), "test"),
    ((8, -2), "blop"),
    ((-5, 90), "mot"),
    ((0, 0), "wat"),
    ((-34, -4), "airport"),
    ((8, 88), "cdg"),
  )
  val tree = KDTree(data, depth=8).get

  it should "return the nearest element correctly" in {

    assert(tree.nearest((3,3)).value == ((3, 2), "bar"))
    assert(tree.nearest((-3,-5)).value == ((0, 0), "wat"))
    assert(tree.nearest((-30,-5)).value == ((-34, -4), "airport"))
  }
}
