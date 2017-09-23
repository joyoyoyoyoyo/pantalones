/**
  * Define Recursive Data for Owners
  *
  * Owner(Username("Stu_Hood"), Owner(Username("Jack"), Owner(Username("jd"), NoneUser))
  * Owner(Username(Stu_Hood),Owner(Username(doe),Owner(Username(jd),NoneUser)))
  */
sealed trait OwnersList
final case object NoneUser extends OwnersList
final case class Owner(head: User, tail: OwnersList) extends OwnersList

sealed trait User
final case class Username(owner: String) extends User

object OwnersList extends App {
  val owners = Owner(Username("Stu_Hood"), Owner(Username("doe"), Owner(Username("jd"), NoneUser)))
  println(owners)
}




