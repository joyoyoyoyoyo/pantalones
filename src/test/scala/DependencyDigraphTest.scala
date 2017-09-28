/**
  * Dependencies graph generated from DEPENDENCIES files
  *
  * Node("message"->"follow")
  * Node("message"->"user")
  *
  * Node("follow"->"user)
  * Node("user")
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

  "./src/com/twitter/message" should "depend on ./src/com/twitter/{follow, user}" in {

  }
}
