
import java.io.File
import java.nio.file.{FileSystems, Files, Paths}
import java.util.concurrent.{Executors, ForkJoinTask, RecursiveTask}
import java.util.concurrent.atomic.AtomicReference

import ReadOnly.ReadOnly
import pantalones.ValidatorCLI

import scala.collection.concurrent.TrieMap
import scala.collection.immutable.Queue
import scala.collection.mutable
import scala.collection.parallel.immutable.ParSeq
import scala.concurrent.ExecutionContext
import scala.util.{DynamicVariable, Either, Success}

//import scala.collection.parallel.mutable.ParTrieMap
import scala.concurrent.Future
import scala.io.{Codec, Source}
//import java.util.concurrent._
//import pantalones.common.{DefaultTaskScheduler, TaskScheduler}
//import java.util.concurrent.RecursiveTask

import java.util.concurrent.ForkJoinPool
import scala.collection.concurrent

object ValidateApprovals extends App {
  val acceptors, changedFiles = ValidatorCLI.parse(args)

  // Enqueue for managing the tasks
  val enqueueAccepts = acceptors.value._1.foldLeft(Queue[String]()){ (acc, elem) => acc.enqueue(elem) }
  val enqueueModified = acceptors.value._2.foldLeft(Queue[String]()){ (acc, elem) => acc.enqueue(elem) }


  // threading and parellism context
  val parallelism = Runtime.getRuntime.availableProcessors * 32
  val forkJoinPool = new ForkJoinPool(parallelism)
  val executionContext = ExecutionContext.fromExecutorService(forkJoinPool)

  // Our persistent data structures
  val cacheTree = concurrent.TrieMap[String, File]()
  val snapshot = cacheTree.snapshot
  val cacheDir = concurrent.TrieMap[String, File]()
  val ownersRepository = concurrent.TrieMap[String, List[String]]()
  val dependenciesRepository = concurrent.TrieMap[String, String]()




  val root = new File(".")
  walkTree(root)(executionContext)
//  traversed

  cacheTree.keySet.foreach(println)
//  val transientDependencies = GraphDAG(dependenciesRepository)


  def parallelTraverse[A, B, C, D](
        localFile: File,
        cacheDirectories: File => Unit,
        cacheOwners: File => Unit,
        cacheDependencies: File => Unit,
        cacheFiles: File => Unit) (implicit ec: ExecutionContext): Future[Unit] = {
    localFile match {
      case directories if directories.isDirectory => { Future.successful(cacheDirectories(directories)) }
      case owners if owners.getName.endsWith(ReadOnly.OWNERS.toString) => { Future.successful(cacheOwners(owners)) }
      case dependencies if dependencies.getName.endsWith(ReadOnly.DEPENDENCIES.toString) => { Future.successful(cacheDependencies(dependencies)) }
//      case files if files.isFile => { Future.successful(handleProjFile(files)) }
    }
  }

  def walkTree(file: File)(implicit ec: ExecutionContext): Iterable[File] = {
    Future { parallelTraverse(file, cacheDirectories, cacheOwners, cacheDependencies, cacheFiles)(ec) }
    val children = new Iterable[File] {
      def iterator = if (file.isDirectory) file.listFiles.iterator else Iterator.empty
    }
    Seq(file) ++: children.flatMap(walkTree)
  }

  // asynchronously cache files in project repository

  def cacheDirectories(file: File) = {
    cacheTree.put(file.getCanonicalPath, file)
  }
  def cacheFiles(file: File) = {
    cacheTree.put(file.getCanonicalPath, file)
  }
  def cacheOwners(file: File) = {
//    val owners = Source.fromFile(ReadOnly.OWNERS.toString)(Codec.UTF8).getLines.toList
//    val paths = Source.fromFile(ReadOnly.DEPENDENCIES.toString)(Codec.UTF8).getLines.toList
//
////    val owners = Source.fromFile(file)(Codec.UTF8).getLines
////    owners.foldLeft(0 until owners.length){ (acc, username) =>
////      ownersRepository.put(username, file.getCanonicalPath :: ownersRepository.getOrElse(username, List()))
////      acc
//    }
    cacheTree.put(file.getCanonicalPath, file)

  }
  def cacheDependencies(file: File) = {
    cacheTree.put(file.getCanonicalPath, file)
  }

//
//  val owners = Future { Source.fromFile(ReadOnly.OWNERS.toString)(Codec.UTF8).getLines }
//  val paths = Future { Source.fromFile(ReadOnly.DEPENDENCIES.toString)(Codec.UTF8).getLines }
//  val transitiveDependencies = ???


//  val localSnapshot = cache.snapshot
//
//  val transitiveSnapshot = cache.snapshot()
//
//  val approvals = Future {
//    args match {
////      case cache.
//    }
//  }
//
//  val changedFiles = Future {
//    Success(cache.get(""))
//  }
//
//  val directoryies = approvals
////  def directories = Future { }
//
//  //
////  val dependenceyGraph =
//
////  val owner = AtomicReference[FileEvent](FileCreated("."))
////
////  AtomicReference
//  // process files in parallel to build dependency graph
////  val parallel =
}

object ReadOnly extends Enumeration {
  type ReadOnly = Value
  val OWNERS, DEPENDENCIES = Value
}
//
//def traverseFiles(patterns: ReadOnly) = {
//
//}

//def cache() = {
//
//}






sealed trait Command
case class GetFileList(dir: String) extends Command
case class CopyFile(srcpath: String, destpath: String) extends Command
case class DeleteFile(path: String) extends Command
case class FindFiles(regex: String) extends Command



sealed trait State

class Idle extends State

class Creating extends State

class Copying(val n: Int) extends State

class Deleting extends State

class Entry(val isDir: Boolean) {
  val state = new AtomicReference[State](new Idle)
}



//  val scheduler = new DynamicVariable[TaskScheduler](new DefaultTaskScheduler)
//  class NewTaskScheduler {
//    scheduler.value.
//  }

//  val open = ParTrieMap[Node, Parent]()
//  val closed = ParTrieMap[Node, Parent]()


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

