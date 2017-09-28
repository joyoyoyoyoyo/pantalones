import scala.util.Success


object ValidatorCLI {

  def parse(args: Array[String], projectPath: String)  = {
    Predef.require(args.length > 0)
    //TODO: Exhaustive checking
    args.toList match {
      case _ :: "--approvers" :: approvalsDelimited :: "--changed-files" :: changedFilesDelimited :: _ => {
        val approvers = approvalsDelimited.split(",").toList
        val changedFiles = changedFilesDelimited.split(",").map {
          file => projectPath + file
        }.toList
        Success(approvers, changedFiles)
      }
      case _ :: "--changed-files" :: changedFilesDelimited :: "--approvers" :: approvalsDelimited :: _ => {
        val approvers = approvalsDelimited.split(",").toList
        val changedFiles = changedFilesDelimited.split(",").map {
          file => projectPath + file
        }.toList
        Success(approvers, changedFiles)
      }
    }
  }
}
