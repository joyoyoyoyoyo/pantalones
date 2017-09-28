import org.scalatest.FeatureSpec

import scala.collection.concurrent.TrieMap
import scala.collection.immutable.Queue

object ValidationApprovalsTest extends FeatureSpec {

  /**
    * At least one approver is required, if the changed file is owned by one of them
  **/
  val waitingApprovers = Queue("alovelace")
  val dirRepo = TrieMap[String, List[String]]("/directory/" -> List("owner1","alovelace","owner2"))
  val ownerRepo = TrieMap[String,List[String]]("alovelace"-> List("/","dir3", "dir4", "/directory/"))
  val filesIAmChanging = List("/directory/")
  val next = waitingApprovers.dequeue._1
  val x = ownerRepo.lookup(next)
  val accepted = x.filter(_.contains(filesIAmChanging.head))
  println(accepted)



}
//class ParenCountSpec extends FeatureSpec
//  with GivenWhenThen {
//
//  feature("Assist with autocorrect on evernote academia") {
//    scenario("Footnotes are corrected with MLA (Prof Elephant 200)")  {
//      Given("We a professor is citing their bibliography")
//      When("""Footnotes are written in MLA (Prof Elephant p.100)""")
//      Then("Unenclosed enclosed footnotes should be identified for future autocorrect")
//    }
//    pending
//  }