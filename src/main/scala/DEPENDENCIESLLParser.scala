import scala.util.matching.Regex
import util.parsing.combinator.RegexParsers

trait DEPENDENCIESLLParser extends RegexParsers {
  override val whiteSpace: Regex = """[\n]""".r
}
