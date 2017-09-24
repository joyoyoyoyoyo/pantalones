import java.io.File

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
abstract sealed class FileEvent[+T] {

  /**
    * Recursive matching
    *
    * //TODO: @code
    *
    * @param action: action performed by the validator, triggered on exeCxt
    * @param exeCtx execution context, to resolve ambiguity on the the thread
    * @tparam FileEvent: a FileEvent ADT for resolving ADT Product types and ADT Or Types
    * @return
    */
  def handleFileEvent[FileEvent](action: FileEvent)(implicit exeCtx: ExecutionContext): (FileEvent) => Future[FileEvent] = {
    this match {
      case FileModified(_) => Future(_)(exeCtx)
      case FileCreated(_) => Future(_)(exeCtx)
      case FileDeleted(_) => Future(_)(exeCtx)
    }
  }
}

trait FileInfo {
  def fileName: String
  def exists: Boolean
  def isDirectory: String
  def canonicalPath: String
  def lastModified: String
  def path: String
  def parent: FileInfo
}

//def refresh(file: File) = { // cache original values
//  val origExists = exists
//  val origLastModified = lastModified
//  val origDirectory = directory
//  val origLength = length
//  // refresh the values
//  name = file.getName
//  exists = file.exists
//  directory = if (exists) file.isDirectory
//  else false
//  lastModified = if (exists) file.lastModified
//  else 0
//  length = if (exists && !directory) file.length
//  else 0
//  // Return if there are changes
//  exists != origExists || lastModified != origLastModified || directory != origDirectory || length != origLength
//}


final case class FileCreated[T](fileName: String) extends FileEvent[T]

final case class FileDeleted[T](fileName: String) extends FileEvent[T]

final case class FileModified[T](fileName: String) extends FileEvent[T]

final case class FileChanged[T](filename: String, fileID: SerialVersionUID) extends FileEvent[T]

//abstract case class FileRead[T](filename: String) extends FileEvent[T]