package events

sealed trait DependencyEvent {
  val name: String
}

final case class DependencyCreated(name: String) {

}
