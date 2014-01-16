package cn.yuanye.concurrence.lock;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class LockTest {
	
	static class TestThread extends Thread{
		private static int count = 0;
		private int id = ++count;
		private CountDownLatch signal = null;
		private UnFairLock lock = null;
		
		public TestThread(CountDownLatch signal,UnFairLock lock){
			this.signal = signal;
			this.lock = lock;
		}
		
		public void doSomething(){
			System.out.println(this + " doSomething");
			try {
				TimeUnit.MILLISECONDS.sleep(500);
			} catch (InterruptedException e) {
				return;
			}
			
		}
		
		@Override
		public void run(){
			try {
				signal.await();
				System.out.println(this + " started");
				
				lock.lock();
				doSomething();
				lock.unlock();
				System.out.println(this + " exited");
				
			} catch (InterruptedException e) {
				return;
			}
			
		}
		
		@Override 
		public String toString(){
			return "thread " + id;
		}
	}
	
	public static void main(String[] args) throws InterruptedException{
		int nthread = 5;
		CountDownLatch start = new CountDownLatch(1);
		UnFairLock lock = new UnFairLock();
		
		for(int i = 0; i < nthread ; i++){
			new TestThread(start,lock).start();;
		}
		
		TimeUnit.MILLISECONDS.sleep(500);
		start.countDown();
		//System.out.println("System exit");
	}
	
}
