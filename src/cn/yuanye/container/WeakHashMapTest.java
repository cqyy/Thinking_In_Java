package cn.yuanye.container;

import java.util.WeakHashMap;

class Element{
	private String id;
	public Element(String id){
		this.id = id;
	}
	@Override
	public String toString(){
		return  this.getClass().getSimpleName()+id;
	}
	
	@Override
	public int hashCode(){
		return id.hashCode();
	}
	
	@Override
	protected void finalize(){
		System.out.println("finalized: "+this.getClass().getSimpleName() + id);
	}
}

class Key extends Element{
	public Key(String id){
		super(id);
	}
}

class Value extends Element{
	public Value(String id){
		super(id);
	}
}

public class WeakHashMapTest {
	public static void main(String[] args){
		WeakHashMap<Key,Value> map = new WeakHashMap<Key,Value>();
		int size = 100;
		Key[] keys = new Key[size];
		for(int i=0;i<size;i++){
			Key k = new Key(String.valueOf(i));
			Value v = new Value(String.valueOf(i));
			if(i%5==0)
				keys[i] = k; ///store a real reference
			map.put(k, v);
		}
		System.gc();
	}
}
