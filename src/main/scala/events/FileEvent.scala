package events

trait FileEvent
case class FileCreated(fileID: SerialVersionUID) extends FileEvent
case class FileDeleted(fileID: SerialVersionUID) extends FileEvent
case class FileChanged(fileID: SerialVersionUID) extends FileEvent