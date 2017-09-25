package pantalones

trait Directory {
  def path: String
  def parent: String
  def ownersFile: String
  def dependentDirectories: List[String]
}

sealed trait AcceptanceRule
//final case class


final case class AcceptorNotFound(username: String)
final case class ModificationFileNotFound(filename: String)
final case class DependentDirectoryFound(directory: String)

///** Constructs a tree of futures where tasks can be reasonably split.
//  */
//def compute(task: Task[R, Tp], depth: Int): Future[Task[R, Tp]] = {
//  if (task.shouldSplitFurther && depth < maxdepth) {
//  val subtasks = task.split
//  val subfutures = for (subtask <- subtasks.iterator) yield compute(subtask, depth + 1)
//  subfutures.reduceLeft { (firstFuture, nextFuture) =>
//  for {
//  firstTask <- firstFuture
//  nextTask <- nextFuture
//} yield {
//  firstTask tryMerge nextTask.repr
//  firstTask
//}
//} andThen {
//  case Success(firstTask) =>
//  task.throwable = firstTask.throwable
//  task.result = firstTask.result
//  case Failure(exception) =>
//  task.throwable = exception
//}
//} else Future {
//  task.tryLeaf(None)
//  task
//}
//}
//
//  compute(topLevelTask, 0) map { t =>
//  t.forwardThrowable()
//  t.result
//}
}