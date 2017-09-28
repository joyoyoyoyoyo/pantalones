import scala.annotation.tailrec
import scala.util.Success
import scala.util.matching.Regex


object ValidatorCLI {
//  val args1 = ""
//  val a2 = "--changed-files"
  val arguments = """(?:--approvers)\s+(.*)(?:--changed-files)\s+(.*)""".r
//  val r = new Regex("""(\d+)\.(\d+)\.(\d+)\.(\d+)""")
  def parse(args: Array[String], projectPath: String)  = {
    Predef.require(args.length > 0)
//    args.partition()
    //TODO: Exhaustive checking

    @tailrec
    def loop(acc : List[String], approvers: List[String], cf: List[String]): (List[String], List[String]) = {
      acc match {
        case Nil => (approvers, cf)
        case "--approvers" :: approversDelimited :: (tail: List[String]) =>
          loop(tail, approversDelimited.split(",").toList ::: approvers, cf)
        case "--changed-files" :: changedFilesDelimited :: tail =>
          loop(tail, approvers, changedFilesDelimited.split(",").map(projectPath + _).toList ::: cf)
      }
    }
    loop(args.toList, Nil, Nil)
  }
}
