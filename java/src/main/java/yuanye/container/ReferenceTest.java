package yuanye.container;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.lang.ref.PhantomReference;
import java.util.LinkedList;

class BigThing{
	private static final int SIZE = 10000;
	@SuppressWarnings("unused")
	private long[] la = new long[SIZE];
	private String id;
	
	public BigThing(String id){
		this.id = id;
	}
	
	@Override
	public String toString(){
		return id;
	}
	
	@Override
	protected void finalize(){
		System.out.println("BigThing "+id+" finalized");
	}
}
public class ReferenceTest {
	private static ReferenceQueue<BigThing> rq = new ReferenceQueue<BigThing>();
	
	public static void checkQueue(){
		Reference<? extends BigThing> r = rq.poll();
		if(r!=null){
			System.out.println("In queue "+r.get());
		}
	}
	
	public static void main(String[] args){
		int size = 10;
		LinkedList<SoftReference<BigThing>> sa = new LinkedList<SoftReference<BigThing>>();
		for(int i = 0;i < size; i++){
			sa.add(new SoftReference<BigThing>(new BigThing("sotf "+i),rq));
			System.out.println("Just Created: " + sa.getLast());
			checkQueue();
		}
		
		LinkedList<WeakReference<BigThing>> sw = new LinkedList<WeakReference<BigThing>>();
		for(int i = 0; i < size ;i++){
			sw.add(new WeakReference<BigThing>(new BigThing("weak "+i),rq));
			System.out.println("Just Created: " + sw.getLast());
			checkQueue();
		}
		
		@SuppressWarnings("unused")
		SoftReference<BigThing> s = new SoftReference<BigThing>(new BigThing("soft"));
		@SuppressWarnings("unused")
		WeakReference<BigThing> w = new WeakReference<BigThing>(new BigThing("weak"));
		System.gc();
		
		LinkedList<PhantomReference<BigThing>> pa = new LinkedList<PhantomReference<BigThing>>();
		for(int i = 0; i < size; i++){
			pa.add(new PhantomReference<BigThing>(new BigThing("phantom "+i),rq));
			System.out.println("Just created: "+pa.getLast());
			checkQueue();
		}
	}
}
