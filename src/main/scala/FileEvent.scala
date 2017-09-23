import scala.concurrent.{ExecutionContext, Future}

/**
  *
  * Most of the patterns I've drawn are from:
  *
  * Bibtex:
  * Concurrent Programming in Scala, p50
  * Youtube: Essential Scala: Six Core Principles, @14:23
  * Scala-lang: https://scala-lang.org/_#
  */
sealed trait FileEvent {

  val fileName: String

  /**
    * Recursive matching
    *
    * //TODO: @code
    *
    * @param action: action performed by the validator, triggered on exeCxt
    * @param exeCxt execution context, to resolve ambiguity on the the thread
    * @tparam FileEvent: a FileEvent ADT for resolving ADT Product types and ADT Or Types
    * @return
    */
  def handleFileEvent[FileEvent](action: FileEvent)(implicit exeCxt: ExecutionContext): (FileEvent) => Future[FileEvent] = {
    this match {
      case FileModified(_) => Future(_)(exeCxt)
      case FileCreated(_) => Future(_)(exeCxt)
      case FileDeleted(_) => Future(_)(exeCxt)
    }
  }
}

final case class FileCreated[FileCreated <: FileEvent](fileName: String) extends FileEvent

final case class FileDeleted(fileName: String) extends FileEvent

final case class FileModified(fileName: String) extends FileEvent

final case class FileChanged(filename: String, fileID: SerialVersionUID)