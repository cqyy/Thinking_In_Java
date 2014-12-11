package yuanye.container;
/*
 * some boxing issues
 * Java automatically converts a primitive type like int into corresponding wrapper class object e.g. 
 * Integer than its called autoboxing  because primitive is boxed into wrapper class 
 * while in opposite case is called unboxing, where an Integer object is converted into primitive int 
 * Read more: http://javarevisited.blogspot.com/2012/07/auto-boxing-and-unboxing-in-java-be.html#ixzz2mKJWcw22
 * */
public class BoxingIssus {
	public static void main(String[] args){

		//Integer iObject = 1;   //autoboxing,from int to Integer
		//int iPrimitive = iObject; //unboxing,form Integer to int
	
		/* 
		 * problems int creating too much wasted object in loop
		 * in this loop,sum +=i,equals tempSum = tempSum + i; sum = new Integer(tempSum);
		 * created about 1000 wasted Object of Integer
		 * */
		
		Integer sum = 0;
		for(int i=0;i<1000;i++){
			sum += i;
		}
		
		
		int i1 = 1;
		int i2 = 1;
		System.out.format("i1 == i2 ? %b\n",(i1==i2) ); //true
		
		
		/*
		 * In Third example which is a corner case in autoboxing, both Integer object are initialized 
		 * automatically due to autoboxing and since Integer.valueOf() method is used to convert int
		 * to Integer and it caches object ranges from -128 to 127, it returns same object both time. 
		 * In short iobj1 and iobj2 are pointing to same object and when we compare two object with == 
		 * operator it returns true without any autoboxing.
		 * Read more: http://javarevisited.blogspot.com/2012/07/auto-boxing-and-unboxing-in-java-be.html#ixzz2mKNb64WM
		 * */
		Integer iobj1 = 1;
		Integer iobj2 = 1;
		System.out.format("iobj1 == iobj2 ? %b\n", (iobj1 == iobj2) ); //true

		Integer one = new Integer(1);
		Integer anotherOne = 1;
		System.out.format("one == anotherOne ? %b \n", (one == anotherOne) ); //false
		
		/*NullPointerException*/
		/*
		Integer count = null;
		if(count < 10){
			System.out.println("count less than 10");
		}
		*/
	}
}
