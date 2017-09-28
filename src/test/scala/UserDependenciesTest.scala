import org.scalatest._

import scala.collection.concurrent

class UserDependenciesTest extends FlatSpec{
  val PATH = "./src/com/twitter/"

  val nodes = Set(
    "./src/com/twitter/message",
    "./src/com/twitter/follow",
    "./src/com/twitter/user",
    "./src/com/twitter/tweet",
  )
  val authorizers = concurrent.TrieMap[String, Set[String]]()

  val edges = Set(
    ("./src/com/twitter/message", "./src/com/twitter/follow"),
    ("./src/com/twitter/message", "./src/com/twitter/user"),
    ("./src/com/twitter/follow", "./src/com/twitter/user"),
    ("./src/com/twitter/tweet", "./src/com/twitter/follow"),
    ("./src/com/twitter/tweet", "./src/com/twitter/user"),
  )
  val dependencies: Digraph[String] = Digraph[String](nodes, edges)

  "./src/com/twitter/message" should "contain owners eclarke and kantonelli" in {
    val owners = Set("eclarke", "kantonelli")
    val messageDep = dependencies.dfs("./src/com/twitter/message")
    val expected = Set("./src/com/twitter/user", "./src/com/twitter/follow", "./src/com/twitter/message")
    assert (messageDep == expected)

  }
}
