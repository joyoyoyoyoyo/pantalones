
import java.io.File
import java.nio.file.{Files, Paths}

import scala.collection.immutable.Queue
import scala.io.Codec

object ValidateApprovals extends App {
  Logger.trace("Starting validate_approvals task")
  val queue = Queue()
  val root = new FileSystem(".")
  val stream_in = scala.io.Source.fromFile(new File("OWNERS"))(Codec.UTF8).getLines().toList
  val owners = Queue.empty[String]./:(stream_in)((acc, elem) => elem :: acc)
  //TODO: Test File not found
  FileSystem.getDependencies(owners)
  owners.map(println)
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