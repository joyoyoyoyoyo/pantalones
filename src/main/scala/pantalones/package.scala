package object Logger {
  import java.time.Instant
  def unix_timestamp() = Instant.now
  def debug(message: String) = println(s"${Logger.unix_timestamp()}:\t$message")
  def warn(message: String) = println(s"${Logger.unix_timestamp()}:\t$message")
  def error(message: String) = sys.error(message=s"${Logger.unix_timestamp()}:\t$message")
  def info(message: String) = println(s"${Logger.unix_timestamp()}:\t$message")
  def trace(message: String) = println(s"${Logger.unix_timestamp()}:\t$message")
}
