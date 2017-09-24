//class DependencyManagementTest {
//  import org.scalatest.FunSuite
//
//  class PowerSetTest extends FunSuite {
//
//    val expectedPowerSets = Map(
//      Dependency(OwnersList(Username("A")) -> Packages()),
//      Dependency(OwnersList(Username("A")) -> Packages()),
//    )
//
//    test("The powerset can be generated with type Ints") {
//      val elements = Set[Any](1,2,3)
//      assert(PowerSet.powerSet(elements) == expectedPowerSets.getOrElse(elements, Nil))
//    }
//
//    test("The powerset function will work with mixed types in a set (Int and Char)") {
//      val elements = Set[Any](5, 3, 'a')
//      assert(PowerSet.powerSet(elements) == expectedPowerSets.getOrElse(elements, Nil))
//    }
//
//    test("The powerset function will work with mixed types in a set (Int and Char) " +
//      "of different order") {
//      val elements = Set[Any](3, 'a', 5)
//      assert(PowerSet.powerSet(elements) == expectedPowerSets.getOrElse(elements, Nil))
//    }
//
//    test("The powerset function will work with mixed types in a set (Int, String, and Char) ") {
//      val elements = Set[Any](-10, "Z", 'a')
//      assert(PowerSet.powerSet(elements) == expectedPowerSets.getOrElse(elements, Nil))
//    }
//
//    test("Int, Chars, and Strings powersets can be generated") {
//      val elements = Set[Any]("Z", -10, 'a')
//      assert(PowerSet.powerSet(elements) == expectedPowerSets.getOrElse(elements, Nil))
//    }
//
//    test("Empty sets are considered to be in a powerset") {
//      val elements = Set[Any]()
//      assert(PowerSet.powerSet(elements) == expectedPowerSets.getOrElse(elements, Nil))
//    }
//
//    test("A powerset has 2^n - 1 number of elements given n elements in a set") {
//      val elements = Set[Any](7, 3, 6)
//      val numPowerset = scala.math.pow(2,elements.size)
//      assert(PowerSet.powerSet(elements).size == numPowerset)
//    }
//
//    test("These powersets should not be equal") {
//      val elements = Set[Any](7, 3, 6)
//      val diff = Set[Any](1, 4, 5)
//      assert(PowerSet.powerSet(elements)!= PowerSet.powerSet(diff))
//    }
//
//    test("An empty set should have size 0") {
//      assert(Set.empty.size == 0)
//      assert(Set.empty.isEmpty)
//
//    }
//
//    test("Invoking head on an empty Set should produce NoSuchElementException") {
//      assertThrows[NoSuchElementException] {
//        Set.empty.head
//      }
//    }
//  }
//}
