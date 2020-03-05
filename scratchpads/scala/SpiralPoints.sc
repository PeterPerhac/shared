import Math._
val maxR = 250
val spins = 10
val segments = 100
val points = (0 to segments).toArray.map{ sidx =>
  val curr : Double = sidx.toDouble / segments
    val a: Double = curr * 2 * PI * spins
    val x: Double = cos(a)*maxR*curr
    val y: Double = sin(a)*maxR*curr
    (x,y)
}.map{ case (x,y) => s"{x: $x, y: $y}" }.mkString("[", ", ", "]")
