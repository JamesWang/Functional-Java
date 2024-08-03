# Functional-Java - some ideas from using Scala
* Using Checked Exceptions in lambda with the help of sneaky/uncheck runtime exceptions.
* Reader/Writer Monad in Java
* Some combinators:
  * Kestrel: λx.y x
  * Kite: λx.y y
  * Flip: λx.λy.(y, x)
  * Y-combinator: λg.(λx.g(x x) (λx.g(x x))
  *  
