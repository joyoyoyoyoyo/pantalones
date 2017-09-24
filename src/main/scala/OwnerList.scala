import scala.util.Success

//sealed trait OwnerList {
//  def head: Owner
//  def tail: OwnersList
//  def lif2(l: OwnerList, r: OwnerList, fn: (Owner) => (User))
//}

//case object EmptyOwnerList$ extends OwnerList {
//  def head = Owner(Nil)
//  def tail = Logger.error("List tail is empty. We can not inductively break this base list")
//}
//final case class Owner(head: User, tail: OwnerList) extends OwnerList

sealed trait Owner {
  def username: String
  def owns: String
  def accept: Boolean
//  def
}
//case object Nil extends User
//final case class Accept(
//   val username: String,
//   val owns: Package,
//) extends User


/**
  * Define Recursive Data for Owners
  *
  * Owner(Username("Stu_Hood"), Owner(Username("Jack"), Owner(Username("jd"), NoneUser))
  * Owner(Username(Stu_Hood),Owner(Username(doe),Owner(Username(jd),NoneUser)))
  */
//object Owner {
//
//  implicit val formatFromFile = """[a-z]+""".r
//
//  def getUser(users: OwnerList, username: String): User = {
//    users match {
//      case Owner(head, tail) if Username(username) == head => head
//      case Owner(head, tail) => getUser(tail, username)
//      case EmptyOwnerList$ => Nil
//    }
//  }
//}



