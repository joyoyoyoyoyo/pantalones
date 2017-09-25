
import java.io.File

import scala.collection.immutable.Queue
import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import scala.io.{Codec, Source}
import java.util.concurrent.ForkJoinPool

import scala.collection.concurrent
import scala.collection.concurrent.TrieMap
import scala.util.{Failure, Success}

object ValidateApprovals extends App {
  val projectPath = new File(".").getCanonicalPath + "/"
  val arg1, arg2 = ValidatorCLI.parse(args, projectPath)

  // Enqueue for managing the tasks
//  val modifiedFiles = arg2.value._2.toList.foldLeft(Set[String]()){ (acc, elem) => Set(elem) ++ acc }

  val acceptors =
  arg1.value._1.foldLeft(List[String]()){ (acc, elem) => elem :: acc }
  val modifiedFiles =
    arg2.value._2.foldLeft(List[String]()){ (acc, elem) => elem :: acc }


  // threading and parellism context
  val parallelism = Runtime.getRuntime.availableProcessors * 32
  val forkJoinPool = new ForkJoinPool(parallelism)
  val executionContext = ExecutionContext.fromExecutorService(forkJoinPool)

  // Our persistent data structures
  val cacheTree = concurrent.TrieMap[String, File]()
  // on a read, we determine a directories dependencies
  val localPathToDependents = concurrent.TrieMap[String, List[String]]()
  val localPathToAuthorizers = concurrent.TrieMap[String, List[String]]()
  val localPathToSuccessors = concurrent.TrieMap[String, String]()
  val snapshot = cacheTree.snapshot
  val cacheDir = concurrent.TrieMap[String, File]()
  val ownersRepository = concurrent.TrieMap[String, List[String]]()
  val dependenciesRepository = concurrent.TrieMap[String, List[String]]()
  val directoryPrivilegesRepo = concurrent.TrieMap[String, List[String]]()



  val root = new File(".")
  walkTree(root)(executionContext)
//  val message = approve(acceptors, modifiedFiles)
//  println(message)
  localPathToSuccessors.foreach(println)
  def approve(acceptors: Set[String], changedFiles: Set[String]) = {



  }


  def parallelTraverse[A, B, C, D](
        localFile: File,
        cacheDirectories: File => Unit,
        cacheOwners: File => Unit,
        cacheDependencies: File => Unit) (implicit ec: ExecutionContext): Future[Unit] = {
    localFile match {
      case directories if directories.isDirectory => { Future.successful(cacheDirectories(directories)) }
      case owners if owners.getName.endsWith(ReadOnly.OWNERS.toString) => { Future.successful(cacheOwners(owners)) }
      case dependencies if dependencies.getName.endsWith(ReadOnly.DEPENDENCIES.toString) => { Future.successful(cacheDependencies(dependencies)) }
    }
  }

  def walkTree(file: File)(implicit ec: ExecutionContext): Iterable[File] = {
    Future { parallelTraverse(file, cacheDirectories, cacheOwners, cacheDependencies)(ec) }
    val children = new Iterable[File] {
      def iterator = if (file.isDirectory) file.listFiles.iterator else Iterator.empty
    }
    Seq(file) ++: children.flatMap(walkTree)
  }

  // asynchronously cache files in project repository
  def cacheDirectories(file: File)(implicit ec: ExecutionContext) = {
    cacheTree.put(file.getCanonicalPath, file)
    val parent = file.getParentFile.getCanonicalFile
    if (!root.getCanonicalFile.equals(parent))
      localPathToSuccessors.put(file.getCanonicalPath, file.getParentFile.getCanonicalPath)

  }
  def cacheFiles(file: File) = {
    cacheTree.put(file.getCanonicalPath, file)
    val parent = file.getParentFile.getCanonicalFile
    if (!root.getCanonicalFile.equals(parent))
    localPathToSuccessors.put(file.getCanonicalPath, file.getParentFile.getCanonicalPath)

  }
  def cacheOwners(file: File)(implicit ec: ExecutionContext) = {
    val owners = { Future.successful(Source.fromFile(file)(Codec.UTF8).getLines.toList) }
    owners.onComplete {
      case Success(authorizedFiles) => {
        val uniqueUsers = localPathToAuthorizers.getOrElse(file.getCanonicalPath, List[String]()) ::: authorizedFiles
        localPathToAuthorizers.put(file.getCanonicalPath, uniqueUsers)
        val parent = file.getParentFile.getCanonicalFile
        if (!root.getCanonicalFile.equals(parent))
        localPathToSuccessors.put(file.getCanonicalPath, file.getParentFile.getCanonicalPath)

      }
    }
  }
  def cacheDependencies(file: File)(implicit ec: ExecutionContext) = { // normalized the project directory format
    val dependencies = { Future.successful(Source.fromFile(file)(Codec.UTF8).getLines.map(path => s"$projectPath$path").toList) }
    //dependenciesRepository.update(file.getCanonicalPath, dependencies. dependenciesRepository.getOrElse(file.getCanonicalPath,List()))
//    if (localPathToAuthorizers.groupBy(identity).toList)
    dependencies.onComplete {
      case Success(list) => {
        val canonicalDependency = localPathToDependents.getOrElse(file.getCanonicalPath, List[String]()) ::: list
        localPathToDependents.put(file.getCanonicalPath, canonicalDependency)
        val parent = file.getParentFile.getCanonicalFile
        if (!root.getCanonicalFile.equals(parent)) {
          localPathToSuccessors.put(file.getCanonicalPath, file.getParentFile.getCanonicalPath)
        }
      }
    }
  }

}

object ReadOnly extends Enumeration {
  type ReadOnly = Value
  val OWNERS, DEPENDENCIES = Value
}