import collection.mutable.Stack
import org.scalatest._

class IntegrationTest extends FlatSpec {

  val providedRuntime: Array[String] = Array("--approvers", "alovelace,ghopper",
    "--changed-files", "src/com/twitter/follow/Follow.java,src/com/twitter/user/User.java")

  /**
    * Node("message"->"follow")
    * Node("message"->"user")
    *
    * Node("follow"->"user)
    * Node("user")
    *
    * [message>follow, message->user, user, follow->user, tweet->follow, tweet->user]
    */
  val dependency = Set[String]("src/com/twitter/follow",
    "src/com/twitter/user")

  "src/com/twitter/message" should "depend on src/com/twitter/{follow, user}" in {



//    assert(dependency.count() === 2)
//    assert(stack.pop() === 1)
  }





}
