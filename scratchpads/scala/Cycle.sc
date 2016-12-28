def cycle[T](seq: Seq[T]): Stream[T] = {
  assert(seq.nonEmpty, "Cannot cycle over an empty sequence!")
  Stream.continually(seq).flatten
}

val fruit = List("apple", "banana", "orange", "mango")

//both yield same output
(cycle(fruit) take 10).force
(Stream.continually(fruit).flatten take 10).force

//this works
(cycle(Nil) take 10).force
//this will melt your cpu
(Stream.continually(Seq()).flatten take 10).force
