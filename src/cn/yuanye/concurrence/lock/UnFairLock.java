package cn.yuanye.concurrence.lock;


public class UnFairLock {
	private volatile boolean isLocked = false;
	private Thread lockedForThread = null;
	
	public synchronized void lock() throws InterruptedException{
		while(isLocked){
			wait();
		}
		lockedForThread = Thread.currentThread();
		isLocked = true;
	}
	
	public synchronized void unlock(){
		if(lockedForThread != Thread.currentThread()){
			throw new IllegalMonitorStateException("Current thread does't hold the lock");
		}
		
		isLocked = false;
		lockedForThread = null;
		this.notifyAll();
	}
	
}
