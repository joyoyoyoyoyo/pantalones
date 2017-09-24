import java.io.File
import java.nio.file.{Files, Path, Paths}
import java.nio.file.attribute.FileTime

import scala.collection.immutable.{Queue, TreeMap}
import scala.io.Codec


class FileSystem(val root: String) {
  val queue = Queue()
  val projectDir = TreeMap[String, String]()
  val stream_in = scala.io.Source.fromFile(new File("OWNERS"))(Codec.UTF8).getLines().toList
  val owners = Queue.empty[String]./:(stream_in)((acc, elem) => elem :: acc)
  owners.foreach(println)

  def initializeContext() = {
    val rootPath = Paths.get(root)
    val rootCtx = projectDir + (rootPath.toAbsolutePath.toString -> "yo")
    val rootAgain = rootCtx + (Paths.get(rootPath.toUri).toString -> "yo")

    rootAgain.foreach(println)
  }
}

object FileSystem {

  /**
    * example code: FileSystemQueries.queryFileLastModified("temp0",Paths.get("0"))
    */

  def queryFileLastModified(name: String, path: Path): FileTime =
    Files.getLastModifiedTime(Paths.get(name))

  //TODO:
  def getDependencies(owners: List[String]) = Nil

  val x = new FileSystem(".")
  x.initializeContext()


}


//import java.nio.file.attribute.FileTime
//import java.nio.file.{Files, Path, Paths}
//
//class ProjectTreeMap {
//
//}
//
//TODO: throw error if DEPENDENCIES cannot be read
//TODO: Logging
//val traverse the tree for a view/snapshot of the current directory


/**
  * For testing:
  *     val temp: Path = Files.createTempFile("test", "-jar")
  *
  */