package pantalones.tasks

import java.util.concurrent.{ForkJoinTask, RecursiveTask}

//import pantalones.{TaskScheduler, forkJoinPool}
//
//class ValidationTask {
//
//}
//
//class DefaultTaskScheduler extends TaskScheduler {
//  def schedule[T](body: => T): ForkJoinTask[T] = {
//    val t = new RecursiveTask[T] {
//      def compute = body
//    }
//    forkJoinPool.execute(t)
//    t
//  }
//}
