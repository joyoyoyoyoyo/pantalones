//——————————————————————————————————————————————————————————————————————————————
// Abstract syntax

// x ∈ Variable  f ∈ FunctionSymbol  p ∈ PredicateSymbol
//    t ∈ Term    ::= x | f(t⋯)
//    g ∈ Goal    ::= p(t⋯) | g1 ⊗ g2 | g1 & g2 | g1 ⊕ g2 | a ⊸ g | a ⊃ g
//                  | !g | ∃x⋯.g | output
//    a ∈ Assume  ::= p(t⋯) | p(t⋯) ⟜ g | &a⋯ | ∀x⋯.a
// prog ∈ Program ::= a⋯ ⊢ g

//import scala.util.parsing.
//class DependenciesFileParser extends Parser {
//
//}

//import scala.util.parsing.combinator._

//class Dependencies extends RegexParser
