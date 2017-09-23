import java.nio.file.{Path, Paths}

import scala.collection.immutable.TreeMap
import scala.concurrent._
import java.util.concurrent.ConcurrentHashMap
class FileSystem(val root: String) {
  val projectDir = TreeMap[String, String]()

  def initializeContext() = {
    val rootPath = Paths.get(root)
    val rootCtx = projectDir + (rootPath.toAbsolutePath.toString -> "yo")
    val rootAgain = rootCtx + (Paths.get(rootPath.toUri).toString -> "yo")

    rootAgain.foreach(println)
  }

}

object FileSystem extends App {
  val x = new FileSystem(".")
  x.initializeContext()
}
