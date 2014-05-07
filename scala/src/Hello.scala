/**
 * Created by Kali on 14-3-23.
 */

object Hello {
  def oncePerSecond(callback:() =>Unit){
    while (true){
      callback();
      Thread sleep 1000;
    }
  }

  def sayHello(){
    println("Hello,Scala")
  }

  def main(args:Array[String]){
    oncePerSecond(sayHello)
  }
}
