import java.nio.file.{Paths}

import scala.collection.immutable.TreeMap
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
