object StringCompression {

  def main(args: Array[String]) {
    def toListOfTuples(cs: Seq[Char]): List[(Char, Int)] = {
      def tuples(cs: Seq[Char], acc: List[(Char, Int)]): List[(Char, Int)] = cs match {
        case Nil => acc
        case c :: Nil => acc :+ (c, 1)
        case c :: rest =>
          val (fst, snd) = cs.span(_ == c)
          (acc :+ (fst.head, fst.length)) ++ tuples(snd, acc)
      }

      tuples(cs, List())
    }

    println(toListOfTuples("Foo Foo the Snoooo".toList).map { case (c, f) => if (f > 1) s"$c{$f}" else c }.mkString)
  }

}
