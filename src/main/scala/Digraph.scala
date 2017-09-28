object Digraph {
  def apply[A](vertices: Set[A], edges: Set[(A, A)]): Digraph[A] = new Digraph(vertices, edges)
}

final case class Digraph[A](vertices: Set[A], edges: Set[(A, A)]) {
  def appendVertex(vertex: A): Digraph[A] = Digraph(this.vertices + vertex, this.edges)
  def appendEdge(edge: (A, A)): Digraph[A] = Digraph(this.vertices, this.edges + edge)

  def adjacent(vertex: A): Set[A] = {
    for((v1, v2) <- edges; if vertex == v1) yield v2
  }

  def dfs(node: A): Set[A] = {
    def search(vertex: A, visited: Set[A]): Set[A] = {
      val accumulator = visited + vertex
      val adjacentNodes = adjacent(vertex).diff(visited)
      Set(vertex) ++ adjacentNodes.flatMap(search(_, accumulator))
    }
    search(node, Set.empty[A])
  }
}