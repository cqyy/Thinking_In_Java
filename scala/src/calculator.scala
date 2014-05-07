
object calculator {

  abstract class Tree
  case class Sum(l:Tree,r:Tree) extends Tree
  case class Var(v:String) extends Tree
  case class Const(c:Int) extends Tree

  type Environment = String => Int

  def eval(t:Tree , env:Environment): Int = t match {
    case Sum(l,r) => eval(l,env) + eval(r,env)
    case Var(v) => env(v)
    case Const(c) => c
  }

  def derive(t:Tree,v:String): Tree = t match {
    case Sum(l,r) => Sum(derive(l,v),derive(l,v))
    case Var(n) if(n == v) => Const(1)
    case _ =>Const(0)
  }

  def main(args:Array[String]){
    val exp:Tree = Sum(Sum(Var("x"),Var("x")),Sum(Const(7),Var("y")))
    val env:Environment = {case "x"=>5 case "y"=> 7}
    println("Expression: " + exp)
    println("Evaluation with x=5, y=7: " + eval(exp, env))
    println("Derivative relative to x:\n " + derive(exp, "x"))
    println("Derivative relative to y:\n " + derive(exp, "y"))
  }

}