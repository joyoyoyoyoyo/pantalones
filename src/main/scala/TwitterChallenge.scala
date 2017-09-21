import scala.concurrent.ExecutionContext
import scala.concurrent._

object TwitterChallenge extends App {
  val snapshot = 0
  val num = Future { 10}
  println(num)
}