import org.scalatest._

import scala.collection.concurrent.TrieMap

class UserDependenciesTest extends FlatSpec{
  val PATH = "./src/com/twitter/"

  val nodes = Set(
    "./src/com/twitter/message",
    "./src/com/twitter/follow",
    "./src/com/twitter/user",
    "./src/com/twitter/tweet",
  )


   val directoryOwners = TrieMap(
   "." -> Set("ghopper"),
   "./tests/com/twitter/tweet" -> Set("alovelace", "ghopper", "mfox"),
   "./tests/com/twitter/follow" -> Set("alovelace", "ghopper"),
   "./src/com/twitter/tweet" -> Set("alovelace", "ghopper", "mfox"),
   "./src/com/twitter/message" -> Set("eclarke", "kantonelli"),
   "./tests/com/twitter/message" -> Set("eclarke", "kantonelli"),
   "./src/com/twitter/follow" -> Set("alovelace", "ghopper")
   )

  val edges = Set(
    ("./src/com/twitter/message", "./src/com/twitter/follow"),
    ("./src/com/twitter/message", "./src/com/twitter/user"),
    ("./src/com/twitter/follow", "./src/com/twitter/user"),
    ("./src/com/twitter/tweet", "./src/com/twitter/follow"),
    ("./src/com/twitter/tweet", "./src/com/twitter/user"),
  )
  val dependencies: Digraph[String] = Digraph[String](nodes, edges)

  "./src/com/twitter/message" should "contain owners eclarke and kantonelli" in {
    val modifiedFiles = List("./src/com/twitter/message/Message.java")
    val owners = Set("eclarke", "kantonelli")


    val directory = java.nio.file.Paths.get(modifiedFiles.head).getParent.toString
    val localUsers = directoryOwners.getOrElse(directory,Set.empty[String])
    assert(localUsers === owners)

    val messageDep = dependencies.dfs(directory)
    val expected = Set("./src/com/twitter/user", "./src/com/twitter/follow", "./src/com/twitter/message")
    assert (messageDep == expected)

  }
}
