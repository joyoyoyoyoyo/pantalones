package pantalones.collections

import pantalones.collections

/***
  * The ConcTree is a parellel data structure to parallelize the write of one operation
  * by Aleksander Prokspokvich (one of my personal favorite Scala Engineers and Academic).
  * The Conc Tree is a parellel data structure for turning concatenation of data.
  * https://en.wikipedia.org/wiki/Conc-Tree_list
  */
sealed trait ConcTree[+T] {

  // The longest path from some root to some leaf
  def level: Int

  // The number of total number of single element values in subtree's
  def size: Int

  // The left subtree
  def left: ConcTree[T]

  // The right subtree
  def right: ConcTree[T]

  // A quantifier for
  def normalized = this
}

sealed trait Leaf[T] extends ConcTree[T] {
  def left = Logger.error("Leaves cannot have children")
  def right = Logger.error("Leaves cannot have children")
}

case object Empty extends Leaf[Nothing] {
  def level = 0
  def size = 0
}

/** A Tree denoting a tree with a single element, the original element may be
  * referred to as "Single", however I created an alias to demonstrate conciseness
  **/
final case class Element[T](value: T) extends Leaf[T] {
  def level = 0
  def size = 1
  override def toString = s"Element($value)"
}

case class <>[T](left: ConcTree[T], right: ConcTree[T]) extends ConcTree[T] {
  val level = 1 + math.max(left.level, right.level)
  val size = left.size + right.size
}
case class Append[+T](left: ConcTree[T], right: ConcTree[T]) extends ConcTree[T] {
  val level = 1 + math.max(left.level, right.level)
  val size = left.size + right.size

//  override def normalized = {
//    def wrap[T](xs: ConcTree[T], ys: ConcTree[T]): ConcTree[T] = (xs: @unchecked) match {
//      case Append(ws, zs) => wrap(ws, zs <> ys)
//      case xs => xs <> ys
//    }
//
//    wrap(left, right)
//  }
}





//object <> extends ConcTree {
//  def apply(xs: Conc[T], i: Int) = xs match {
//
//  }
//}

//object ConcTree {
//
//}



//object Snapshot {
//
//}
//
//trait IterableOps[A, CC[_], C] {
//  // the current one (already implemented)
//  def groupBy[K](f: A => K): Map[K, C]
//
//  // equivalent to groupBy(f).mapValues(_.map(g)) but more efficient
//  def groupMap[K, B](f: A => K)(g: A => B): Map[K, CC[B]]
//
//  // equivalent to groupBy(f).mapValues(_.map(g).reduce(h)) but more efficient
//  def groupMapReduce[K, B](f: A => K)(g: A => B)(h: (B, B) => B): Map[K, B]
//}