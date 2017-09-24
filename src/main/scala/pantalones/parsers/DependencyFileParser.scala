package pantalones.parsers

// https://stackoverflow.com/questions/29499381/what-is-a-triemap-and-what-is-its-advantages-disadvantages-compared-to-a-hashmap/38700133#38700133

/**
  * Language: (a )*a
  * Examples: "a", "a a", "a a a a a a", etc.
  * Grammar:
  *   S -> S 'a' | 'a'
  * The same grammar, written differently:
  *   S -> 'a' | S 'a'
  * A third grammar:
  *   S -> 'a' S | 'a'
**/
class DependencyFileParser {
  import scala.util.Either

}
