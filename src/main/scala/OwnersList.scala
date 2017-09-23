/**
  * Define Recursive Data for Owners
  *
  * Owner(Username("Darkwingduck"), Owner(Username("Jack"), Owner(Username("jd"), NoneUser))
  * Owner(Username(Darkwingduck),Owner(Username(doe),Owner(Username(jd),NoneUser)))
  */
sealed trait OwnersList
final case object NoneUser extends OwnersList
final case class Owner(head: User, tail: OwnersList) extends OwnersList

sealed trait User
final case class Username(owner: String) extends User

object OwnersList extends App {
  val owners = Owner(Username("Darkwingduck"), Owner(Username("doe"), Owner(Username("jd"), NoneUser)))
  println(owners)
}




