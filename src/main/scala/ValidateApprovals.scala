
import java.io.File

import scala.collection.immutable.Queue
import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import scala.io.{Codec, Source}
import java.util.concurrent.ForkJoinPool

import scala.collection.concurrent
import scala.util.{Failure, Success}

object ValidateApprovals extends App {
  val acceptors, changedFiles = ValidatorCLI.parse(args)

  // Enqueue for managing the tasks
  val enqueueAccepts = acceptors.value._1.foldLeft(Queue[String]()){ (acc, elem) => acc.enqueue(elem) }
  val enqueueModified = acceptors.value._2.foldLeft(Queue[String]()){ (acc, elem) => acc.enqueue(elem) }


  // threading and parellism context
  val parallelism = Runtime.getRuntime.availableProcessors * 32
  val forkJoinPool = new ForkJoinPool(parallelism)
  val executionContext = ExecutionContext.fromExecutorService(forkJoinPool)
  val projectPath = new File(".").getCanonicalPath + "/"

  // Our persistent data structures
  val cacheTree = concurrent.TrieMap[String, File]()
  val snapshot = cacheTree.snapshot
  val cacheDir = concurrent.TrieMap[String, File]()
  val ownersRepository = concurrent.TrieMap[String, List[String]]()
  val dependenciesRepository = concurrent.TrieMap[String, List[String]]()
  val directoryPrivilegesRepo = concurrent.TrieMap[String, List[String]]()



  val root = new File(".")
  walkTree(root)(executionContext)

  ownersRepository.keySet.foreach(println)
  dependenciesRepository.keySet.foreach(println)

  val x = approve(enqueueAccepts,enqueueModified)
  println(x.value)
  def approve(acceptors: Queue[String], changedFiles: Queue[String]) = {

    def recurseOnOwner(currOwner: String, acceptors: Queue[String], changedFiles:Queue[String], cf: String): Success[String] = {

      val owns = ownersRepository.lookup(currOwner)
      val dependencies = directoryPrivilegesRepo.getOrElse(cf, List())
      if (dependencies.contains(owns)) Success("Approved")
      if (acceptors.size == 1 && acceptors.size != 1) {
        recurseOnOwner(currOwner, acceptors, changedFiles.dequeue._2, changedFiles.dequeue._1)
      }
      if (acceptors.size != 1 && changedFiles.size == 1) {
        recurseOnOwner(acceptors.dequeue._1, acceptors.dequeue._2, changedFiles, cf)
      }
      if (acceptors.size != 1 && changedFiles.size!= 1) {
        recurseOnOwner(acceptors.dequeue._1, acceptors.dequeue._2, changedFiles.dequeue._2, changedFiles.dequeue._1)
      }
      else {
        Success("Insufficient Approvals")
      }
    }

    recurseOnOwner(acceptors.dequeue._1, acceptors.dequeue._2, changedFiles.dequeue._2, changedFiles.dequeue._1)
  }


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
    val owners = Source.fromFile(file)(Codec.UTF8).getLines.toTraversable

    owners.foreach( user =>
      ownersRepository.update(user, file.getCanonicalPath :: ownersRepository.getOrElse(user,List()) ))

    val directoryPrivileges = owners.toList
    directoryPrivilegesRepo.update(file.getCanonicalPath, directoryPrivileges ::: directoryPrivilegesRepo.getOrElse(file.getCanonicalPath,List()))

    cacheTree.put(file.getCanonicalPath, file)

  }
  def cacheDependencies(file: File) = { // normalized the project directory format
    val dependencies = Source.fromFile(file)(Codec.UTF8).getLines.map(path => s"$projectPath$path").toList
    dependenciesRepository.update(file.getCanonicalPath, dependencies ::: dependenciesRepository.getOrElse(file.getCanonicalPath,List()))
    cacheTree.put(file.getCanonicalPath, file)
  }

}

object ReadOnly extends Enumeration {
  type ReadOnly = Value
  val OWNERS, DEPENDENCIES = Value
}