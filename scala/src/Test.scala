import scala.annotation.tailrec

/**
 * Created by Kali on 14-4-28.
 */
object Test {

  @tailrec
  final def tailResursion(i:Integer){
    if (i != 0){
      println(i)
      tailResursion(i-1)
    }
  }

  def main(args: Array[String]) {
    fa(4)
  }

}
