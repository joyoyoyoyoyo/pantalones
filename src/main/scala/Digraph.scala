object Digraph {
  def apply[A](vertices: List[A], edges: List[(A, A)]): Digraph[A] = new Digraph(vertices, edges)
}

final case class Digraph[A](vertices: List[A], edges: List[(A, A)]) {
  def appendVertex(vertex: A): Digraph[A] = Digraph(vertex :: this.vertices, this.edges)
  def appendEdge(edge: (A, A)): Digraph[A] = Digraph(this.vertices, edge :: this.edges)

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



//  lazy val edges =

