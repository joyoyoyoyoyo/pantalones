package events

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

  def handleFileEvent[FileEvent](action: FileEvent)(implicit exeCxt: ExecutionContext): Future[Int] = {
    this match {
      case FileModified(_) => Future(10)
      case FileCreated(_) => Future(20)
      case FileDeleted(_) => Future(30)
    }
  }
}

final case class FileCreated[FileCreated <: FileEvent](fileName: String) extends FileEvent

final case class FileDeleted(fileName: String) extends FileEvent

final case class FileModified(fileName: String) extends FileEvent

//final case class Owner(username: String)


//trait FileEvent
//case class FileCreated(fileID: SerialVersionUID) extends FileEvent
//case class FileDeleted(fileID: SerialVersionUID) extends FileEvent
//case class FileChanged(fileID: SerialVersionUID) extends FileEvent

