/**
  * Dependencies graph generated from DEPENDENCIES files
  *
  * Node("message"->"follow")
  * Node("message"->"user")
  *
  * Node("follow"->"user)
  *
  * Node("tweet"->"follow")
  * Node("tweet"->"user")
  *
  * Node("user")
  *
  * [message>follow, message->user, user, follow->user, tweet->follow, tweet->user]
  */

import org.scalatest._


class DependencyDigraphTest extends FlatSpec {

  val PATH = "./src/com/twitter/"

  val nodes = Set(
    "./src/com/twitter/message",
    "./src/com/twitter/follow",
    "./src/com/twitter/user",
    "./src/com/twitter/tweet",
  )

  val edges = Set(
    ("./src/com/twitter/message", "./src/com/twitter/follow"),
    ("./src/com/twitter/message", "./src/com/twitter/user"),
    ("./src/com/twitter/follow", "./src/com/twitter/user"),
    ("./src/com/twitter/tweet", "./src/com/twitter/follow"),
    ("./src/com/twitter/tweet", "./src/com/twitter/user"),
  )
  val dependencies: Digraph[String] = Digraph[String](nodes, edges)

  /**
    * At least one approver is required, if the changed file is owned by one of them
    **/
  val providedRuntime = Array("--approvers", "alovelace,ghopper",
    "--changed-files", "src/com/twitter/follow/Follow.java,src/com/twitter/user/User.java")


  "./src/com/twitter/message" should "depend on ./src/com/twitter/{follow, user}" in {
    val messageDep = dependencies.dfs("./src/com/twitter/message")
    val expected = Set("./src/com/twitter/user", "./src/com/twitter/follow", "./src/com/twitter/message")
    assert (messageDep == expected)
  }

  "./src/com/twitter/message" should "contain owners eclarke and kantonelli" in {
    val owners = Set("eclarke", "kantonelli")
    val messageDep = dependencies.dfs("./src/com/twitter/message")
    val expected = Set("./src/com/twitter/user", "./src/com/twitter/follow", "./src/com/twitter/message")
    assert (messageDep == expected)
  }
}
