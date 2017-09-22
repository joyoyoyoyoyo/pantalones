import java.nio.file._
import java.util
import java.util.Collection

import scala.collection.{JavaConverters, immutable}

object LiveWatchService extends App {
  val path = Paths.get(".")
    val watchService = path.getFileSystem.newWatchService
    path.register(
      watchService,
        StandardWatchEventKinds.ENTRY_MODIFY,
          StandardWatchEventKinds.ENTRY_DELETE,
            StandardWatchEventKinds.ENTRY_CREATE)

    def watch(watchService: WatchService) = {
      @scala.annotation.tailrec
      def loop(watchS: WatchService, key: WatchKey) {
        key match {
          case key: WatchKey =>
            import scala.collection.JavaConverters._
            val watchEvent: util.List[WatchEvent[_]] = key.pollEvents()
            println(s"Event type: ${watchEvent.get(0).kind()}\tFile: ${watchEvent.get(0).context()}")
//            println(watchEvent.get(0).context())
          case _ => println("Error")
        }
        key.reset()
        loop(watchS, watchS.take())
      }
      loop(watchService, watchService.take())
    }
    watch(watchService)
}
