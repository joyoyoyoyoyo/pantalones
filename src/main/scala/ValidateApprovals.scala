
import java.io.File

import scala.collection.immutable.Queue
import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import scala.io.{Codec, Source}
import java.util.concurrent.{ConcurrentHashMap, ForkJoinPool}

import ValidateApprovals.acceptors

import scala.collection.concurrent
import scala.collection.concurrent.TrieMap
import scala.util.{Failure, Success, Try}

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
  localPathToSuccessors.foreach(println)

  val validation = traversePrecommitFiles(acceptors, modifiedFiles)
  val approvalList = Map[String, Boolean]()

//  validation.message()


  def traversePrecommitFiles(approvers: List[String], precommitFiles: List[String]) = {

    precommitFiles match {
      case changedFile :: pendingFiles => {
        // read USERS file and get the list of users
        val cwd = localPathToAuthorizers.getOrElse(changedFile, List())

        // check if at least one user can approve the current file
//        checkDirectApprovers(changedFile, cwd)
      }
    }
  }

//  def checkDirectApprovers(fileChanged: String, users: List[String]): List[String] = {
//
//    users match {
//
//    }
//
//    // Rule 1
//    def loopUsrs(users: List[String], fileChanged) = {
//      users match {
//        case user :: pending => if(localPathToDependents.getOrElse(fileChanged))
//      }
//    }
//    loopUsers(cwd, fileChanged)
//
//    // Rule2
//  }

//  def checkTransitiveDependencies()

//  def findApprovers(approving: String, approvers: List[String], file: String) = {
//    approving match {
//      case _ :: Nil => Nil
//      case head :: tail => {
//      }
//      case _ => println
//
//    }
//  }
//  def findDependencies(approvals: List[String], precommitFiles: List[String], changedFile: String): List[String] = {
//    approvals match {
//      case found => {
//        Success("Valid")
//        findApprovers(approvers.head, approvers.tail, changedFile)
//        findDependencies(approvals,precommitFiles.tail, precommitFiles, found)
//        Nil
//      }
//      case _ => Nil
//    }
//  }

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
    val parent = file.getCanonicalFile.getParentFile.getCanonicalPath
    if (!root.getCanonicalFile.getName.equals(parent))
      localPathToSuccessors.put(file.getCanonicalPath, file.getParentFile.getCanonicalPath)

  }
  def cacheFiles(file: File) = {
    cacheTree.put(file.getCanonicalPath, file)
    val parent = file.getCanonicalFile.getParentFile.getCanonicalPath
    if (!root.getCanonicalFile.getName.equals(parent))
    localPathToSuccessors.put(file.getCanonicalPath, file.getParentFile.getCanonicalPath)

  }

  /**
    * Create an association between the current directory (canonical name) and the list of owners in that directory
    *
    * Example: src/com/twitter/message/USERS -> List["src/com/twitter/follow", "src/com/twitter/user"]
    *
    * @param file: DEPENDENCIES file in the current directory
    * @param ec: Threading context
    */
  def cacheOwners(file: File)(implicit ec: ExecutionContext) = {
    val owners = { Future.successful(Source.fromFile(file)(Codec.UTF8).getLines.toList) }
    owners.onComplete {
      case Success(authorizedUsers) => {
        val directoryPath = file.getCanonicalPath.substring(0, file.getCanonicalPath.length - ReadOnly.OWNERS.toString.length - 1)
        val uniqueUsers = localPathToAuthorizers.getOrElse(directoryPath, List[String]()) ::: authorizedUsers
        localPathToAuthorizers.put(directoryPath, uniqueUsers)
        val parent = file.getCanonicalFile.getParentFile.getCanonicalPath
        if (!root.getCanonicalFile.getName.equals(parent))
        localPathToSuccessors.put(directoryPath, parent)
      }
    }
  }

  /**
    * Create an association between the current directory and the directories listed on the dependency list
    *
    * Example: src/com/twitter/message/Dependencies -> List["eclarke","kantonelli"]
    *
    * @param file: USERS file
    * @param ec: Threading context
    */
  def cacheDependencies(file: File)(implicit ec: ExecutionContext) = { // normalized the project directory format
    val dependencies = { Future.successful(Source.fromFile(file)(Codec.UTF8).getLines.map(path => s"$projectPath$path").toList) }
    dependencies.onComplete {
      case Success(dependencyList) => {
        val directoryPath = file.getCanonicalPath.substring(0, file.getCanonicalPath.length - ReadOnly.DEPENDENCIES.toString.length - 1)
        val canonicalDependency = localPathToDependents.getOrElse(directoryPath, List[String]()) ::: dependencyList
        localPathToDependents.put(directoryPath, canonicalDependency)
        val parent = file.getCanonicalFile.getParentFile.getCanonicalPath
        if (!root.getCanonicalFile.getName.equals(parent))
          localPathToSuccessors.put(directoryPath, parent)
      }
    }
  }

}

object ReadOnly extends Enumeration {
  type ReadOnly = Value
  val OWNERS, DEPENDENCIES = Value
}