import java.io.File
import java.nio.file.Path

import scala.io.Source

  sealed case class FileInformation(file: File) {
    def fileItem: File = file

    def parent: File = file.getParentFile

    def path: Path = file.toPath

    def isDirectory: Boolean = file.isDirectory
  }

  sealed trait GraphNode {
    def formsACycle = false

    def isVisited = false

    def isSourceNode = false

    def isDrainNode = false

    def edges = Set()
  }

  final case class UsersFile() extends FileInformation(file = File) with GraphNode {
    val users: Set[String] = Source.fromFile(file).getLines().toSet
  }

  final case class Dependency(dependencies: Set[String] = Set[String]()) extends FileInformation(file = File) with GraphNode

  final case class DAGEdge(from: GraphNode, to: GraphNode)
