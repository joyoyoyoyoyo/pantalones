import java.util.concurrent.{ConcurrentHashMap, LinkedBlockingQueue}

import scala.concurrent.{ExecutionContext, Future}
import scala.io.Codec

object Lifecycle extends App {
  /**
    * Use SIPS algorithm to disjointly inspect classpaths, will find cycles, will find walks
    *
    * Dependency classes as separate classes:
    * Library: A
    * Library: B
    * Sources Root: C
    **/

  val queue = new LinkedBlockingQueue[String]()
  val root = new FileSystem(".")
  val stream_in = scala.io.Source.fromFile("./src/main/scala/Lifecycle.scala")(Codec.UTF8)
  for (line <- stream_in.getLines())
    queue.put(line)
  queue.forEach(println(_))
  
  val y = 10
  val exe = ExecutionContext.Implicits.global

  class KVStore[K, V] {
    val store = new ConcurrentHashMap[K, V]()

    def sawFileCreated = Future.successful(FileCreated("").handleFileEvent(println("Hi"))(exe))
    def sawFileModified = FileModified("").handleFileEvent(println("how"))(exe)
    def sawFileDeleted = FileDeleted("").handleFileEvent(println("are"))(exe)
  }
}
