package snapshot

import java.util.concurrent.atomic.AtomicInteger

sealed trait Snapshot {
  val clock: Int
//  val pid: Int
//  val eventNum: Int
}
final case class LocalSnapshot(clock: Int) extends Snapshot

final case class GlobalSnapshot(clock: Int) extends Snapshot