import java.nio.file.{Paths, StandardWatchEventKinds, WatchKey, WatchService}

object LiveWatchService {
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
            val watchEvent = key.pollEvents()
            println(watchEvent.get(0).context())
          case _ => println("Error")
        }
        key.reset()
        loop(watchS, watchS.take())
      }
      loop(watchService, watchService.take())
    }
    watch(watchService)
}
