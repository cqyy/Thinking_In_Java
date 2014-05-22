package yuanye.container;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class FailFast {
	
	public static void main(String[] args){
		Collection<String> c = new ArrayList<String>();
		Iterator<String> it = c.iterator();
		c.add("hello");
		try{
			it.next();
		}catch(RuntimeException e){
			e.printStackTrace();
		}
	}	
}
