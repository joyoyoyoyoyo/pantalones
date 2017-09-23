
import scala.collection.immutable.Queue
import scala.io.Codec

object ValidateApprovals extends App {
  val queue = Queue()
  val root = new FileSystem(".")
  val stream_in = scala.io.Source.fromFile(s"../../Desktop/${"repo_root/OWNERS"}")(Codec.UTF8).getLines().toList
  val owners = Queue.empty[String]./:(stream_in)((acc, elem) => elem :: acc)
  FileSystem.getDependencies(owners)
  owners.map(println)
}
