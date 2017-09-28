package object Logger {
  import java.time.Instant
  def unix_timestamp(): Instant = Instant.now
  def debug(msg: String): Unit = println(s"${Logger.unix_timestamp()}:\t${Thread.currentThread.getName}\t$msg")
  def warn(msg: String): Unit = println(s"${Logger.unix_timestamp()}:\t${Thread.currentThread.getName}\t$msg")
  def error(msg: String): Nothing = sys.error(message=s"${Logger.unix_timestamp()}:\t${Thread.currentThread.getName}\t$msg")
  def info(msg: String): Unit = println(s"${Logger.unix_timestamp()}:\t${Thread.currentThread.getName}\t$msg")
  def trace(msg: String): Unit = println(s"${Logger.unix_timestamp()}:\t${Thread.currentThread.getName}\t$msg")
}
