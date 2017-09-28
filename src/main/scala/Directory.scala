sealed trait Owner {
  def owners: Set[String]
}
sealed trait Dependency {
  def dependencies: Set[String]
}

final case class Directory(path: String) extends Owner with Dependency{
  def owners = Set[String]()
  def dependencies = Set[String]()
  override def toString: String = path
}
