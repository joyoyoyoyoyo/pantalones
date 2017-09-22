import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger

import scala.concurrent.{ExecutionContext, Future}
import events._
import snapshot.{GlobalSnapshot, LocalSnapshot}

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

  val myNumber = LocalSnapshot(0)
  val global = GlobalSnapshot(0)
  //val globalSnapshot = CI() // points to travis server

  println("Hello World")
  val x = FileCreated("")
//  currentThread().sleep(1000)
  println(x)
}

//type FileChanged = FileModified
