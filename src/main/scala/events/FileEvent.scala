package events

import java.io.File
import java.nio.file.{Files, Path, Paths}
import java.util.concurrent.atomic.AtomicReference

import scala.concurrent.{ExecutionContext, Future}
import scala.io.Source

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

object FindLastModified extends App {
  val temp: Path = Files.createTempFile("test", "-jar")
  val file = Paths.get("temp0")
  val time = Files.getLastModifiedTime(file)
  val sample = Paths.get("build.sbt")
  println(sample)
  println(time)
}

//import scala.concurrent._
//sealed trait FileEvent
//case class FileCreated(fileID: SerialVersionUID) extends FileEvent
//case class FileDeleted(fileID: SerialVersionUID) extends FileEvent
//case class FileChanged(fileID: SerialVersionUID) extends FileEvent
//final case class Owner(username: String)


//trait FileEvent
//case class FileCreated(fileID: SerialVersionUID) extends FileEvent
//case class FileDeleted(fileID: SerialVersionUID) extends FileEvent
//case class FileChanged(fileID: SerialVersionUID) extends FileEvent

