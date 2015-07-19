/**
 * Created by Kali on 14-4-28.
 * CBN(call-by-name) and CBV(call-by-value)，CBV terminates then CBN does,but the other direction is not ture
 * Scala adopt the CBV as default.
*/
object CBV {

  def loop:Int = loop;                    //loop expression

  def first(x:Int , y: => Int) = 1;       //x -> CBV,y -> CBN


  def main(args:Array[String]){
    println(first(1+2,loop));            //terminates
    println(first(loop,1+2));            //does not terminate
  }
}