/**
  * Logger utility
  */
package object Logger {
  import java.time.Instant
  def unix_timestamp() = Instant.now
  def debug(msg: String) = println(s"${Logger.unix_timestamp()}:\t${Thread.currentThread.getName}\t$msg")
  def warn(msg: String) = println(s"${Logger.unix_timestamp()}:\t${Thread.currentThread.getName}\t$msg")
  def error(msg: String) = sys.error(message=s"${Logger.unix_timestamp()}:\t${Thread.currentThread.getName}\t$msg")
  def info(msg: String) = println(s"${Logger.unix_timestamp()}:\t${Thread.currentThread.getName}\t$msg")
  def trace(msg: String) = println(s"${Logger.unix_timestamp()}:\t${Thread.currentThread.getName}\t$msg")
}
