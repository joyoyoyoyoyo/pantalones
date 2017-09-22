import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger
import java.nio.file._

import scala.concurrent.{ExecutionContext, Future}
import events._

import ExecutionContext.Implicits.global._
import snapshot.{GlobalSnapshot, LocalSnapshot}

import scala.collection.JavaConverters

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
  /**
    * Use SIPS algorithm to disjointly inspect classpaths, will find cycles, will find walks
    *
    * Dependency classes as separate classes:
    * Library: A
    * Library: B
    * Sources Root: C
    **/



  val myNumber = LocalSnapshot(0)
  val global = GlobalSnapshot(0)

  val y = 10
  val exe = ExecutionContext.Implicits.global


  FileCreated("").handleFileEvent(println("Hi"))(exe)
  FileModified("").handleFileEvent(println("how"))(exe)
  FileDeleted("").handleFileEvent(println("are"))(exe)
  FileModified("").handleFileEvent(println("you"))(exe)

  FileDeleted("").handleFileEvent(println("doing"))(exe)

//  import scala.util.parsing.inp
//  currentThread().sleep(1000)
}

// p 729
//def elem(kind: String, p: Elem => Boolean) = {
//  new Parser[Elem] {
//    def apply(in: Input) =
//      if (p(in.first)) Success(in.first, in.rest)
//      else Failure(kind + " expected", in) //TODO: ValidationFailure extends Failure
//  }
//}

//type FileChanged = FileModified


//TODO: Canonical traverse
// expected == action