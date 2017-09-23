import java.nio.file.{Path, Paths}

import scala.collection.immutable.TreeMap
import scala.concurrent._
import java.util.concurrent.ConcurrentHashMap
class FileSystem(val root: String) {
  val projectDir = TreeMap[String, String]()

  def initializeContext() = {
    val rootPath = Paths.get(root)
    val getRootContext = projectDir + (rootPath.getFileName.toString -> "yo")
    projectDir.foreach(println)
  }

}

object FileSystem extends App {
  val x = new FileSystem(".")
  x.initializeContext()
}
