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

  val nodes = List(
    "./src/com/twitter/message",
    "./src/com/twitter/follow",
    "./src/com/twitter/user",
    "./src/com/twitter/tweet",
    "./src/com/twitter/ux"
  )

  val edges = List(
    ("./src/com/twitter/message", "./src/com/twitter/follow"),
    ("./src/com/twitter/message", "././src/com/twitter/user"),
    ("./src/com/twitter/follow", "./src/com/twitter/user"),
    ("./src/com/twitter/user", ""),
    ("./src/com/twitter/tweet", "./src/com/twitter/follow"),
    ("./src/com/twitter/tweet", "./src/com/twitter/user"),
    ("./src/com/twitter/ux", "./src/com/twitter/message")
  )

  val dependencies: Digraph[String] = Digraph[String](nodes, edges)

  /**
    * At least one approver is required, if the changed file is owned by one of them
    **/
  val providedRuntime: Array[String] = Array("--approvers", "alovelace,ghopper",
    "--changed-files", "src/com/twitter/follow/Follow.java,src/com/twitter/user/User.java")


  "./src/com/twitter/message" should "depend on ./src/com/twitter/{follow, user}" in {
    val readDependencies = dependencies.dfs("./src/com/twitter/message")
    val x = dependencies.dfs("./src/com/twitter/ux")
  }
}
