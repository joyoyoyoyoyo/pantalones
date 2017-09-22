package events

sealed trait FileEvent {
  val fileName: String
  def handleFileEvent[FileEvent](action:FileEvent): Int = {
    this match {
      case FileModified(_) => 10
      case FileCreated(_) => 20
      case FileDeleted(_) => 30
    }
  }
}

final case class FileCreated[FileCreated <: FileEvent](fileName: String) extends FileEvent
final case class FileDeleted(fileName: String) extends FileEvent
final case class FileModified(fileName: String) extends FileEvent

//trait FileEvent
//case class FileCreated(fileID: SerialVersionUID) extends FileEvent
//case class FileDeleted(fileID: SerialVersionUID) extends FileEvent
//case class FileChanged(fileID: SerialVersionUID) extends FileEvent

