import scala.util.Success

sealed trait OwnersList
final case object EmptyOwnersList extends OwnersList
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

  def getOwner(users: OwnersList, username: String): User = {
    users match {
      case Owner(head, tail) if Username(username) == head => head
      case Owner(head, tail) => getOwner(tail, username)
      case EmptyOwnersList => Nil
    }
  }
}

object OwnersList extends App {
  val owners = Owner(Username("Stu_Hood"), Owner(Username("doe"), Owner(Username("jd"), EmptyOwnersList)))

  println(Owner.getOwner(owners,"jd"))
}




