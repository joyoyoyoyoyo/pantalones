import scala.util.Success

sealed trait OwnersList {
//  def head: Owner
//  def tail: OwnersList
}

case object EmptyOwnersList extends OwnersList {
//  def head = Owner(Nil)
//  def tail = Logger.error("List tail is empty. We can not inductively break this base list")
}
final case class Owner(head: User, tail: OwnersList) extends OwnersList

sealed trait User
final case object Nil extends User
final case class Username(owner: String) extends User


/**
  * Define Recursive Data for Owners
  *
  * Owner(Username("Stu_Hood"), Owner(Username("Jack"), Owner(Username("jd"), NoneUser))
  * Owner(Username(Stu_Hood),Owner(Username(doe),Owner(Username(jd),NoneUser)))
  */
object Owner {

  implicit val formatFromFile = """[a-z]+""".r

  def getUser(users: OwnersList, username: String): User = {
    users match {
      case Owner(head, tail) if Username(username) == head => head
      case Owner(head, tail) => getUser(tail, username)
      case EmptyOwnersList => Nil
    }
  }
}



