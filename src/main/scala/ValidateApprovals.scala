
import java.io.File
import java.nio.file.{Files, Paths}

import scala.collection.immutable.Queue
import scala.collection.parallel.mutable.ParTrieMap
import scala.concurrent.{ExecutionContext, Future}
import scala.io.Codec
//import java.util.concurrent._

//import pantalones.common.{DefaultTaskScheduler, TaskScheduler}
//import java.util.concurrent.RecursiveTask
import java.util.concurrent.ForkJoinPool
import java.util.concurrent.ForkJoinPool

import scala.collection.concurrent
import scala.util.DynamicVariable

object ValidateApprovals extends App {
  val forkJoinPool = new ForkJoinPool
  val scheduler = new DynamicVariable[TaskScheduler](new DefaultTaskScheduler)

  class NewTaskScheduler {
    scheduler.value.
  }

  val open = ParTrieMap[Node, Parent]()
  val closed = ParTrieMap[Node, Parent]()

}

//  val results = ParTrieMap()
//  val owners = (1 until 100)
//  val directories = (1 until 100)
//  val authorizers = collection.parallel.mutable.ParTrieMap(owners zip directories: _* ) map { case (k, v) => (k.toDouble, v.toDouble) }
//
//  val dependencies = f(directories)
//  val traverseTree =
//  def readCLI() = {
//
//  }
//
//  def main() = {
//
//    val source = scala.io.Source.fromFile("DEPENDENCIES")
//    source.toSeq.indexOfSlice("myKeyword")
//  }
//    val usdQuote = Future { connection.getCurrentValue(USD) }
//    val chfQuote = Future { connection.getCurrentValue(CHF) }
//
//    val purchase = for {
//      usd <- usdQuote
//      chf <- chfQuote
//      if isProfitable(usd, chf)
//    } yield connection.buy(amount, chf)
//
//    purchase onSuccess {
//      case _ => println("Purchased " + amount + " CHF")
//    }
//
//
//
//    val usdQuote = Future {
//      connection.getCurrentValue(USD)
//    } map {
//      usd => "Value: " + usd + "$"
//    }
//    val chfQuote = Future {
//      connection.getCurrentValue(CHF)
//    } map {
//      chf => "Value: " + chf + "CHF"
//    }
//
//    val anyQuote = usdQuote fallbackTo chfQuote
//
//    anyQuote onSuccess { println(_) }
//  }
//}

//sealed trait TaskManager {
//  val forkJoinPool = new ForkJoinPool
//
//  def parallel() =
//  val users = task { fetchDependencies() }
//  val owners = task { fetchOwners() }
//  val dependencies =
//val owners = Future {
//  connection.getCurrentValue(USD)
//}
//
//  owners onComplete { case quote =>
//    val purchase = Future {
//      if (isProfitable(quote)) connection.buy(amount, quote)
//      else throw new Exception("not profitable")
//    }
//
//    purchase onSuccess {
//      case _ => println("Purchased " + amount + " USD")
//    }
//  }
//
//}


//  /**
//    * Recursive matching
//    *
//    * //TODO: @code
//    *
//    * @param task: action performed by the validator, triggered on exeCxt
//    * @param exeCxt execution context, to resolve ambiguity on the the thread
//    * @tparam FileEvent: a FileEvent ADT for resolving ADT Product types and ADT Or Types
//    * @return
//    */
//  def handleTask[FileEvent](task: FileEvent)(implicit exeCxt: ExecutionContext): (FileEvent) => Future[FileEvent] = {
//    this match {
//      case FetchDependencies(_) => Future(_)(exeCxt)
//      case FetchUsers(_) => Future(_)(exeCxt)
//    }
//  }
//}


  // instantiate in memory model
//  val forkJoinPool = new
//  val cache = new concurrent.TrieMap[Int, String]()
//  val approvers = new concurrent.TrieMap[User, String]()
//  val transients = new concurrent.TrieMap[Directory]()
//val depedencies
  //  val snapshot = cache.snapshot()

  // do action in memory

//  new AtomicReference[List[A]](Nil)
//  cache {
//    parse(freq, "johnny 121") match {
//      case Success(matched,_) => println(matched)
//      case Failure(msg,_) => println("FAILURE: " + msg)
//      case Error(msg,_) => println("ERROR: " + msg)
//    }
//  }

//  val snapshot =


//  def main(args: Array[String]): Unit = {
//
//  }
//object ValidateApprovals extends App {
//  val stream = getClass.getResourceAsStream("someclassdata")
//  assert(stream != null)
//
//
//
//  def parallel[A, B, C, D](taskA: => A, taskB: => B, taskC: => C, taskD: => D): (A, B, C, D) = {
//    val ta = task { taskA }
//    val tb = task { taskB }
//    val tc = task { taskC }
//    val td = taskD
//    (ta.join(), tb.join(), tc.join(), td)
//  }
////
//  Logger.trace("Starting validate_approvals task")
//  //val settings = Context()
//  // val context
//  val root = new FileSystem(".")
//  //TODO: Test File not found
////  FileSystem.map(depedency -> owner)
//}

