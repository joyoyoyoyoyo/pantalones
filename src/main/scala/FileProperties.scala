import java.util.Observable

sealed trait FileProperties {
  val name: String
  val path: String
  val successor: String
  val predecessors: String
  val created: String
  val modified: String
}
//new Observable.in
case class FileINode(dir: String) extends FileProperties