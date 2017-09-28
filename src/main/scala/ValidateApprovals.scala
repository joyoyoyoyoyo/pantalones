
import java.io.File

import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import scala.io.{Codec, Source}
import java.util.concurrent.ForkJoinPool

import scala.collection.concurrent.TrieMap
import scala.util.{Success}

object ValidateApprovals extends App {
  /**
    * Parse program arguments
    */
  val projectPath = new File(".").getCanonicalPath + "/"
  val arg1, arg2 = ValidatorCLI.parse(args, projectPath)
  val acceptors = arg1._1.foldLeft(List[String]()){ (acc, elem) => elem :: acc }
  val modifiedFiles = arg2._2.foldLeft(List[String]()){ (acc, elem) => elem :: acc }


  /**
    * Use local threading & parallelism context from local machine for speedup (workers)
    */
  val parallelism = Runtime.getRuntime.availableProcessors * 32
  val forkJoinPool = new ForkJoinPool(parallelism)
  implicit val executionContext = ExecutionContext.fromExecutorService(forkJoinPool)

  /**
    * Out in memory model data structures
    * =====================================
    * We use two TrieMaps for caching until the Digraphs are built for dependency resolution
    *   1. for caching directory dependencies, during the filesystem walk
    *   2. for caching users (authorizers), during the filesystem walk
    *
    * These concurrent TrieMaps are a concurrent thread-safe lock-free data structure
    * They have an O(1) snapshot operation.
    * The use of the TrieMap's main advantage in this usage is that we can guarantee that
    * our iterators stay consistent in a concurrent run (This means we won't have collisions).
    *
    * References: https://www.researchgate.net/publication/221643801_Concurrent_Tries_with_Efficient_Non-Blocking_Snapshots
    *
    */
  val localPathToDependents = TrieMap[String, List[String]]()
  val localPathToAuthorizers = TrieMap[String, Set[String]]()


  /**
    * Note: Walking the tree is parallelized
    * 1. Walk the local project filesystem.
    * 2. Upon arrival of a USERS file, we create an association between the directory and list of users
    *    Example: TrieMap("./src/com/twitter/follow", Set("alovelace", "ghopper")
    * 3. Upon arrival of a DEPENDENCIES file, we create an association between the directory and lists of dependencies
    *    (This includes transitive dependencies)
    * 4. After walking the filesystem for this project, continue main function
    *
    */
  val root = new File(".")
  walkTree(root)(executionContext)

  def parallelTraverse[A, B, C, D](
        localFile: File,
        cacheOwners: File => Unit,
        cacheDependencies: File => Unit) (implicit ec: ExecutionContext): Future[Unit] = {
    localFile match {
      /** asynchronously cache files in project repository **/
      case owners if !owners.isDirectory && owners.getName.endsWith(ReadOnly.OWNERS.toString) => { Future.successful(cacheOwners(owners)) }
      case dependencies if !dependencies.isDirectory && dependencies.getName.endsWith(ReadOnly.DEPENDENCIES.toString) => { Future.successful(cacheDependencies(dependencies)) }
    }
  }

  def walkTree(file: File)(implicit ec: ExecutionContext): Iterable[File] = {
    Future { parallelTraverse(file, cacheOwners, cacheDependencies)(ec) }
    val children = new Iterable[File] {
      def iterator = if (file.isDirectory) file.listFiles.iterator else Iterator.empty
    }
    Seq(file) ++: children.flatMap(walkTree)
  }


  /**
    * Create an association between the current directory and the directories listed on the dependency list
    *
    * Example: src/com/twitter/message/Dependencies -> List["eclarke","kantonelli"]
    *
    * @param file: USERS file
    * @param ec: Threading context
    */
  def cacheOwners(file: File)(implicit ec: ExecutionContext): Unit = {
    val owners = { Future.successful(Source.fromFile(file)(Codec.UTF8).getLines.toList) }
    owners.onComplete {
      case Success(authorizedUsers) => {
        val canonicalDirectory = file.getCanonicalPath.substring(0, file.getCanonicalPath.length -
          ReadOnly.OWNERS.toString.length - 1)
        val uniqueUsers = localPathToAuthorizers.getOrElse(canonicalDirectory, Set[String]()) ++ authorizedUsers
        localPathToAuthorizers.put(canonicalDirectory, uniqueUsers)
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
  def cacheDependencies(file: File)(implicit ec: ExecutionContext): Unit = { // normalized the project directory format
    val dependencies = { Future.successful(Source.fromFile(file)(Codec.UTF8).getLines.map(path => s"$projectPath$path").toList) }
    dependencies.onComplete {
      case Success(dependencyList) => {
        val canonicalDirectory = file.getCanonicalPath.substring(0, file.getCanonicalPath.length -
          ReadOnly.DEPENDENCIES.toString.length - 1)
        val canonicalDependency =
          localPathToDependents.getOrElse(canonicalDirectory, List[String]()) ::: dependencyList
        localPathToDependents.put(canonicalDirectory, canonicalDependency)
      }
    }
  }


  /***
    * Build Dependency Graphs for DEPENDENCIES & USERS
    */
  val dirEdges = localPathToDependents.keys.foldLeft(Set.empty[(String, String)]) { (edgesAcc,e1) =>
    val destination = localPathToDependents.getOrElse(e1, Set())
    if (destination.nonEmpty) { edgesAcc ++ destination.map((e1,_)) } else edgesAcc
  }
  val dirNodes = localPathToDependents.keys.toSet
  val dependencyGraph: Digraph[String] = new Digraph(dirNodes, dirEdges)

  val userDependencies = localPathToAuthorizers.keys.foldLeft(Set.empty[(String,String)]) { (edgesAcc,e1: String) =>
    val users: List[String] = localPathToAuthorizers.getOrElse(e1, Set.empty[String]).toList
    if (users.nonEmpty) { edgesAcc ++ users.flatMap(user => Set[(String, String)]((e1 ,user))) } else edgesAcc }
  val userNodes = localPathToAuthorizers.keys.toSet
  val userDigraph: Digraph[String] = new Digraph(userNodes, userDependencies)


  /**
    * Validate our program arguments to check if the requirements were met for approval
    */
  val output = validate(acceptors.toSet, modifiedFiles.toSet)
  println(output)

  executionContext.shutdown()
  /**
    * Loop through the list of arguments provided and search for the appropriate dependencies
    *   and users
    *
    * @return Accepted or Insufficient approvals
    */
  def validate(approvals: Set[String], changedFiles: Set[String]): String = {
    val validationMap = TrieMap[String, Boolean]()
    val mine = approvals.foldLeft(Set.empty[String]) { (acc, proposedAcceptor) => {
      changedFiles.foldLeft(acc) { (fileAcc, file) =>
        val directory = java.nio.file.Paths.get(file).getParent.toString
        val digraph = dependencyGraph.dfs(directory)
        digraph.foldLeft(fileAcc) { (dirAcc, dep) => {
          val users = localPathToAuthorizers.getOrElse(dep.toString, Set())
          if (users.contains(proposedAcceptor))
            validationMap.put(proposedAcceptor, true)
        }
          fileAcc
        }
        acc
      }
    }
    }
    if (validationMap.values.count(_ == true) == approvals.size)
      "Accepted"
    else
      "Insufficient approvals"
  }



}
