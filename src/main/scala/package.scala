/**
  * Logger utility
  */
package object Logger {
  import java.time.Instant
  def unix_timestamp() = Instant.now
  def debug(msg: String) = println(s"${Logger.unix_timestamp()}: ${Thread.currentThread.getName} $msg")
  def warn(msg: String) = println(s"${Logger.unix_timestamp()}: ${Thread.currentThread.getName} $msg")
  def error(msg: String) = sys.error(message=s"${Logger.unix_timestamp()}: ${Thread.currentThread.getName} $msg")
  def info(msg: String) = println(s"${Logger.unix_timestamp()}: ${Thread.currentThread.getName} $msg")
  def trace(msg: String) = println(s"${Logger.unix_timestamp()}: ${Thread.currentThread.getName} $msg")
}
