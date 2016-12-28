import scala.collection.mutable
import scala.collection.mutable.Stack

object Solution {

  val bracketMap = Map('(' -> ')', '[' -> ']', '{' -> '}')
  lazy val brackets = bracketMap.flatMap(tuple => List(tuple._1, tuple._2)).toList
  val isOpenBracket: (Char) => Boolean = bracketMap.keySet.contains(_)

  def isClosingBracket(closingBracketCandidate: Char, openBracket: Char): Boolean = bracketMap.toList contains (openBracket -> closingBracketCandidate)

  def balanced(brackets: List[Char]): Boolean = {
    val bracketStack = mutable.Stack[Char]()
    var result = true
    brackets.foreach {
      b => {
        if (isOpenBracket(b)) bracketStack.push(b)
        else if (isClosingBracket(b, bracketStack.headOption.getOrElse(0x00))) bracketStack.pop()
        else result = false
      }
    }
    result && bracketStack.isEmpty
  }

  def balancedBrackets(bracketString: String): Boolean = {
    val bracketsOnly = bracketString.toList
    assert(bracketsOnly.length > 0, "expecting a non-empty list of brackets")
    if (bracketsOnly.length % 2 != 0) false
    else
      balanced(bracketsOnly)
  }

  def main(args: Array[String]) {
    val sc = new java.util.Scanner(System.in);
    val t = sc.nextInt();
    sc.nextLine()
    val arr: Array[String] = new Array(t)
    val range: Range = 0 until t

    range.foreach((i) => {
      val line = sc.nextLine()
      arr(i) = line
    })

    arr.foreach(s => println(if (balancedBrackets(s)) "YES" else "NO"))
  }
}
