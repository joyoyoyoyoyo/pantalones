import scala.util.matching.Regex
import scala.util.parsing.combinator.RegexParsers


import util.parsing.combinator.RegexParsers

trait OWNERSParser extends RegexParsers {
  override val skipWhitespace = false
  override val whiteSpace = """[ \t]""".r

  def file: Parser[List[List[String]]] = rep1(row) ^^ (rows => rows)
  def row: Parser[List[String]] = repsep(field, ",") <~ """\r""".? <~ "\n"
  def field: Parser[String] = TEXT ||| STRING | EMPTY

  lazy val TEXT: Parser[String] = rep1("""[^,\n\r\"]""".r) ^^ makeText
  lazy val STRING: Parser[String] = whiteSpace.* ~> "\"" ~> rep("\"\"" | """[^\"]""".r) <~ "\"" <~ whiteSpace.* ^^ makeString
  lazy val EMPTY: Parser[String] = "" ^^ makeEmpty

  def makeText: (List[String]) => String
  def makeString: (List[String]) => String
  def makeEmpty: String => String
}

trait CSVParserAction {
  // remove leading and trailing blanks
  def makeText = (text: List[String]) => text.mkString("").trim
  // remove embracing quotation marks
  // replace double quotes by single quotes
  def makeString = (string: List[String]) => string.mkString("").replaceAll("\"\"", "\"")
  // modify result of EMPTY token if required
  def makeEmpty = (string: String) => ""
}

trait CSVParserIETFAction extends CSVParserAction {
  // no trimming of WhiteSpace
  override def makeText = (text: List[String]) => text.mkString("")
}

import java.io.FileReader

object CSVParserCLI extends OWNERSParser with CSVParserIETFAction {
  def main(args: Array[String]) {
    val reader = new FileReader(args(0))
    println(parseAll(file, reader))
  }
}