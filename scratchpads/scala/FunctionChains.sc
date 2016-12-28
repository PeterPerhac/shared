Function.chain(Seq[Int => Int](
  _ + 2, _ * 2, _ - 3, _ / 3
))(10)

Function.chain(Seq[String => String](
  _.toLowerCase, _ + " PETERIK", _.dropRight(2), "Foo " + _
))("SOMAR")

def words(s: String): Seq[String] = s.split("\\s+")

val peterFrajer = "Peter Perhac Je Fest Velky Frajer"

(words _ andThen (_ map (_.reverse)) andThen (_ mkString " ")) (peterFrajer)

