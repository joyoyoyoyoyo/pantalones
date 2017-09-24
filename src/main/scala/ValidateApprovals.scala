
import java.io.File
import java.nio.file.{Files, Paths}

import scala.collection.immutable.Queue
import scala.io.Codec

object ValidateApprovals extends App {
  Logger.trace("Starting validate_approvals task")
  //val settings = Context()
  // val context
  val root = new FileSystem(".")
  //TODO: Test File not found
//  FileSystem.map(depedency -> owner)
}

object Logger {
  import java.time.Instant
  def unix_timestamp() = Instant.now
  def debug(msg: String) = println(s"${Logger.unix_timestamp()}:\t$msg")
  def warn(msg: String) = println(s"${Logger.unix_timestamp()}:\t$msg")
  def error(msg: String) = println(s"${Logger.unix_timestamp()}:\t$msg")
  def info(msg: String) = println(s"${Logger.unix_timestamp()}:\t$msg")
  def trace(msg: String) = println(s"${Logger.unix_timestamp()}:\t$msg")
}