//sealed trait Digraph

//final case class Node[+T](value: T)
//
//final case class Digraph[T] {
//  val nodes: Set[T]
//}
//
//sealed trait Node[+T] {
//  def adjacent: List[Edge] = Nil
//  def isVisited = false
//}
//final case class Edge[A <: T, B <: T ,+T](from: Node[A], to: Node[B]) extends Node[T]
//final case class DependencyGraph[Edge[A, B] <: Node[T], +T](nodes: Set[T], edges: Set[Edge]) extends Node [T]{
//  val nodes
//}


final case class Digraph[A](vertices: List[A], edges: List[(A, A)]) {
  def appendVertex(vertex: A): Digraph[A] = Digraph(vertex :: this.vertices, this.edges)
  def append(edge: (A, A)): Digraph[A] = Digraph(this.vertices, edge :: this.edges)

  def adjacent(vertex: A): List[A] = {
    for((v1, v2) <- edges; if vertex == v1) yield v2
  }

  def dfs(node: A): List[A] = {
    def search(vertex: A, visited: Set[A]): List[A] = {
      val accumulator = visited + vertex
      val adjacentNodes = adjacent(vertex).filterNot(visited.contains)
      adjacentNodes.flatMap(search(_, accumulator)) ::: List(vertex)
    }
    search(node, Set.empty[A]).distinct
  }
}

object Digraph {
  def apply[A](vertices: List[A], edges: List[(A, A)]): Digraph[A] = new Digraph(vertices, edges)

  def apply[A](vertices: List[A], edges: List[(A, A)]): Digraph[A] = new Digraph(vertices, for ( (a,b) <- edges ) yield (a, b))
}

//  lazy val edges =


