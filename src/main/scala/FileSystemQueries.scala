
import java.nio.file.attribute.FileTime
import java.nio.file.{Files, Path, Paths}


object FileSystemQueries {
  def queryFileLastModified(name: String, path: Path): FileTime =
    Files.getLastModifiedTime(Paths.get(name))
}

object FindLastModified extends App {
  FileSystemQueries.queryFileLastModified("temp0",Paths.get("0"))
}

/**
  * For testing:
  *     val temp: Path = Files.createTempFile("test", "-jar")
  *
  */
