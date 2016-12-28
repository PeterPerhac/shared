object RepeatUntil {

  class Repeater(command: => Unit) {
    def UNTIL(condition: => Boolean) {
      command
      if (condition) () else UNTIL(condition)
    }
  }

  def REPEAT(command: => Unit): Repeater = new Repeater(command)

  def main(args: Array[String]): Unit = {
    var x = 0
    REPEAT {
      x += 1;
      println(x)
    } UNTIL (x == 10)
  }

}
