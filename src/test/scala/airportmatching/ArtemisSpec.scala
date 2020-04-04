import org.scalatest.flatspec.AnyFlatSpec
import airportmatching._
import airportmatching.Artemis

class ArtemisSpec extends AnyFlatSpec {

  behavior of "the Artemis tree"

  val data: List[Airport] = List(
    Airport("foo", Point(1f, 3f)),
    Airport("bar", Point(3f, 2f)),
    Airport("baz", Point(3f, 1f)),
    Airport("test", Point(8f, 9f)),
    Airport("blop", Point(8f, -2f)),
    Airport("mot", Point(-5f, 90f)),
    Airport("wat", Point(0f, 0f)),
    Airport("airport", Point(-34f, -4f)),
    Airport("cdg", Point(8f, 88f)),
  )

  it should "return the nearest element correctly" in {

    val tree = Artemis(data, depth=8).get

    assert(tree.nearest(Point(3f, 3f)).value == ("bar", (3f, 2f)))
    assert(tree.nearest(Point(-3f, -5f)).value == ("wat", (0f, 0f)))
    assert(tree.nearest(Point(-30f, -5f)).value == ("airport", (-34f, -4f)))
  }

  val airports: List[Airport] = List(
    Airport("FEL", Point(48.2056f, 11.26614f)),
  )

  it should "return the nearest airport" in {

    val tree = Artemis(airports, depth=8).get
    val user = User("test", Point(44.9748f, 5.0264f))
    println(tree.nearest(user.location).value)
  }
}
