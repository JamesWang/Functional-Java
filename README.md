# Functional-Java
### Implement and use Reader monad for dependency injection with lazy evaluation
- An example usage case will be, in a case of using xPath to extract different tag values from XML, we can be first 
  to build the algos for extracting each required xPath, then at the place where XML available, inject the extractor
  which carries the incoming data

```
    //build a List of the following object(lazy evaluate wrapped function)
    //ValueExtractor will be instantiate once the XML available
    //then the List of the ReaderM can be either evaluated sequencially or in parallel
    ReaderM<ValueExtractor, O>

```

# Functional-Java - some ideas from using Scala
* Using Checked Exceptions in lambda with the help of sneaky/uncheck runtime exceptions.
* Reader/Writer Monad in Java
* Some combinators:
  * Kestrel: λx.y x
  * Kite: λx.y y
  * Flip: λx.λy.(y, x)
  * Y-combinator: λg.(λx.g(x x) (λx.g(x x))
  *