import java.nio.file._
import java.util

/**
  * Currently unused: this is a life watch service for detecting if a file is created, modified, or deleted
  */
object LiveWatchService {
  val root = Paths.get(".")

  def watching(path: Path): WatchService = {
    val ws = path.getFileSystem.newWatchService
    path.register(
      ws,
      StandardWatchEventKinds.ENTRY_MODIFY,
      StandardWatchEventKinds.ENTRY_DELETE,
      StandardWatchEventKinds.ENTRY_CREATE)
    ws
  }

  val ws = watching(path = root)


  def watch(watchService: WatchService) = {
    @scala.annotation.tailrec
    def loop(watchS: WatchService, key: WatchKey) {
      key match {
        case key: WatchKey =>
          val watchEvent: util.List[WatchEvent[_]] = key.pollEvents()
          println(s"Event type: ${watchEvent.get(0).kind()}\tFile: ${watchEvent.get(0).context()}")
        case _ => println("Error")
      }
      key.reset()
      loop(watchS, watchS.take())
    }

    loop(watchService, watchService.take())
  }

  watch(ws)
}
