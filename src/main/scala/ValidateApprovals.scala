
import java.io.File
import java.nio.file.{Files, Paths}

import scala.collection.immutable.Queue
import scala.io.Codec
import java.util.concurrent._

import pantalones.common.{DefaultTaskScheduler, TaskScheduler}
//import java.util.concurrent.RecursiveTask
//import java.util.concurrent.ForkJoinPool
//import scala.concurrent.
import scala.util.DynamicVariable

object ValidateApprovals extends App {

  def taskA( => )

}
//object ValidateApprovals extends App {
//  val stream = getClass.getResourceAsStream("someclassdata")
//  assert(stream != null)
//
//
//
//  def parallel[A, B, C, D](taskA: => A, taskB: => B, taskC: => C, taskD: => D): (A, B, C, D) = {
//    val ta = task { taskA }
//    val tb = task { taskB }
//    val tc = task { taskC }
//    val td = taskD
//    (ta.join(), tb.join(), tc.join(), td)
//  }
////
//  Logger.trace("Starting validate_approvals task")
//  //val settings = Context()
//  // val context
//  val root = new FileSystem(".")
//  //TODO: Test File not found
////  FileSystem.map(depedency -> owner)
//}

