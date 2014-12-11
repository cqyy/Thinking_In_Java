/**
 * Created by Kali on 14-5-13.
 */
object factory{
  def sum(f:Int => Int)(a:Int,b:Int):Int ={
    def cac(a:Int,acc:Int):Int={
      if (a > b) acc
      else cac(a+1,f(a)+acc)
    }
    cac(a,0)
  }

  sum(x=>x)(3,5)
}