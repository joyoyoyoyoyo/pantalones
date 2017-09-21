import java.util.concurrent.Executors
import scala.concurrent.{ExecutionContext, Future}

// The app will begin by creating a daemon process in the
//    background
// The solution monitors multiple environments
// The command validate acts as a pre-commit
// The accept response is the post-commit
//  import ExecutionContext.Implicits.global
//  val x = Future { 10 }
//  val y = Executors.newCachedThreadPool()
//  println(x)
//  val processors: Int = java.lang.Runtime.getRuntime.availableProcessors()
//  val base: Int = 8 * processors * processors
object Lifecycle extends App {

  val snapshot = 0
  println("Hello World")
//  val graph = new Grap

}