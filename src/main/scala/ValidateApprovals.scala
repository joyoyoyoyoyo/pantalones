
import java.io.File

import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import scala.io.{Codec, Source}
import java.util.concurrent.ForkJoinPool

import scala.collection.concurrent
import scala.util.{Success, Try}

object ValidateApprovals extends App {
  val projectPath = new File(".").getCanonicalPath + "/"
  val arg1, arg2 = ValidatorCLI.parse(args, projectPath)
  val acceptors = arg1._1.foldLeft(List[String]()){ (acc, elem) => elem :: acc }
  val modifiedFiles = arg2._2.foldLeft(List[String]()){ (acc, elem) => elem :: acc }


  // threading and parellism context
  val parallelism = Runtime.getRuntime.availableProcessors * 32
  val forkJoinPool = new ForkJoinPool(parallelism)
  val executionContext = ExecutionContext.fromExecutorService(forkJoinPool)

  // Our persistent data structures
  // on a read, we determine a directories dependencies
  val localPathToDependents = concurrent.TrieMap[String, List[String]]()
  val localPathToAuthorizers = concurrent.TrieMap[String, List[String]]()
  val localPathToSuccessors = concurrent.TrieMap[String, String]()

  val root = new File(".")
  walkTree(root)(executionContext)

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
    val parent = file.getParentFile.getCanonicalFile
    if (!root.getCanonicalFile.equals(parent))
      localPathToSuccessors.put(file.getCanonicalPath, file.getParentFile.getCanonicalPath)

  }


  /**
    * Create an association between the current directory and the directories listed on the dependency list
    *
    * Example: src/com/twitter/message/Dependencies -> List["eclarke","kantonelli"]
    *
    * @param file: USERS file
    * @param ec: Threading context
    */
  def cacheOwners(file: File)(implicit ec: ExecutionContext) = {
    val owners = { Future.successful(Source.fromFile(file)(Codec.UTF8).getLines.toList) }
    owners.onComplete {
      case Success(authorizedUsers) => {
        val canonicalDirectory = file.getCanonicalPath.substring(0, file.getCanonicalPath.length -
          ReadOnly.OWNERS.toString.length - 1)
        val uniqueUsers = localPathToAuthorizers.getOrElse(canonicalDirectory, List[String]()) ::: authorizedUsers
        localPathToAuthorizers.put(canonicalDirectory, uniqueUsers)
        val parent = file.getParentFile.getCanonicalFile
        if (!root.getCanonicalFile.equals(parent))
          localPathToSuccessors.put(canonicalDirectory, file.getParentFile.getCanonicalPath)

      }
    }
  }

  /**
    * Create an association between the current directory (canonical name) and the list of owners in that directory
    *
    * Example: src/com/twitter/message/Dependencies -> List["src/com/twitter/follow", "src/com/twitter/user"]
    *
    * @param file: DEPENDENCIES file in the current directory
    * @param ec: Threading context
    */
  def cacheDependencies(file: File)(implicit ec: ExecutionContext) = { // normalized the project directory format
    val dependencies = { Future.successful(Source.fromFile(file)(Codec.UTF8).getLines.map(path => s"$projectPath$path").toList) }
    dependencies.onComplete {
      case Success(dependencyList) => {
        val canonicalDirectory = file.getCanonicalPath.substring(0, file.getCanonicalPath.length -
          ReadOnly.DEPENDENCIES.toString.length - 1)
        val canonicalDependency =
          localPathToDependents.getOrElse(canonicalDirectory, List[String]()) ::: dependencyList
        localPathToDependents.put(canonicalDirectory, canonicalDependency)
        val parent = file.getParentFile.getCanonicalFile
        if (!root.getCanonicalFile.equals(parent)) {
          localPathToSuccessors.put(canonicalDirectory, file.getParentFile.getCanonicalPath)
        }
      }
    }
  }


  /***
    * Build Dependency Graphs
    */
  val dirEdges = localPathToDependents.keys.foldLeft(Set.empty[(String, String)]) { (edgesAcc,e1) =>
    val destination = localPathToDependents.getOrElse(e1, Set())
    if (destination.nonEmpty) { edgesAcc ++ destination.map((e1,_)) } else edgesAcc
  }
  val dirNodes = localPathToDependents.keys.toSet
  val dependencyGraph: Digraph[String] = new Digraph(dirNodes, dirEdges)

  val userDependencies = localPathToAuthorizers.keys.foldLeft(Set.empty[(String,String)]) { (edgesAcc,e1: String) =>
    val users: List[String] = localPathToAuthorizers.getOrElse(e1, List.empty[String])
    if (users.nonEmpty) { edgesAcc ++ users.flatMap(user => Set[(String, String)]((e1 ,user))) } else edgesAcc }
  val userNodes = localPathToAuthorizers.keys.toSet
  val userDigraph: Digraph[String] = new Digraph(userNodes, userDependencies)


  // Acceptance Check
  val validationMap = Map[String, Boolean]().withDefaultValue(false)
  acceptors.foreach { proposedAcceptor => {
    modifiedFiles.foreach { file =>
      val directory = java.nio.file.Paths.get(modifiedFiles.head).getParent.toString
      val dependencies = dependencyGraph.dfs(directory)
      dependencies.foreach { dep =>
        val users = localPathToAuthorizers.getOrElse(dep.toString, Nil)
        if (users.contains(proposedAcceptor))
          validationMap.updated(proposedAcceptor, true)
      }
    }
  }
  }

  if(validationMap.values.count(_ == false) == 0)
    println("Accepted")
  else
    println("Insufficient approvals")




}

object ReadOnly extends Enumeration {
  type ReadOnly = Value
  val OWNERS, DEPENDENCIES = Value
}