sealed trait ValidationError {

}

case class PermissibleError(name: String) extends ValidationError