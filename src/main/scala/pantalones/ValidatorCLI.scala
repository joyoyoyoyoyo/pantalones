package pantalones

import scala.util.{Failure, Success}


object ValidatorCLI {
  def parse(args: Array[String]): Success[(Array[String], Array[String])]  = {
    val approversRegex = raw"\\s+--approvers\\s+(.+)\\s+--changed-files".r.pattern
    args.toList match {
      case "--approvers" :: approvalsDelimited :: "--changed-files" :: changedFilesDelimited :: _ => {
        val approvers = approvalsDelimited.split(",")
        val changedFiles = changedFilesDelimited.split(",")
        Success(approvers, changedFiles)
      }
      case "--changed-files" :: changedFilesDelimited :: "--approvers" :: approvalsDelimited :: _ => {
        val approvers = approvalsDelimited.split(",")
        val changedFiles = changedFilesDelimited.split(",")
        Success(approvers, changedFiles)
      }
    }
  }
}
