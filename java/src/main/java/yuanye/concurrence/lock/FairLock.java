package yuanye.concurrence.lock;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * A fair lock,following FIFO
 * 
 * @author YuanYe
 * */

class LockObject{
	private volatile boolean isNotified = false;
	
	/**
	 * wait until the {@value isNotified} is true
	 * @throws InterruptedException 
	 * */
	public synchronized void doWait() throws InterruptedException{
		while(!isNotified){
			wait();
		}
		
		isNotified = false;
	}
	
	/**
	 * notify thread blocked in the doWait
	 * */
	public synchronized void doNotify(){
		isNotified = true;
		notify();
	}
	
	@Override
	public boolean equals(Object o){
		return (o == this);
	}
}


public class FairLock {
	private volatile boolean isLocked = false;
	private Thread lockedThread = null;
	private List<LockObject> locks = new LinkedList<LockObject>();
	
	public void lock() throws InterruptedException {
		LockObject lock = new LockObject();
		boolean isAvaliable = false;

		synchronized (this) {
			locks.add(lock);
		}

		while (!isAvaliable) {
			synchronized (this) {
				isAvaliable = !isLocked && locks.get(0) == lock;

				if (isAvaliable) {
					isLocked = true;
					locks.remove(0);
					lockedThread = Thread.currentThread();
					return;
				}
			}
			try {
				lock.doWait();
			} catch (InterruptedException e) {
				synchronized (this) {
					locks.remove(lock);
				}
				throw e;
			}
		}

	}

	public synchronized void unlock(){
		if(Thread.currentThread() != lockedThread){
			throw new IllegalMonitorStateException(
					"Calling thread has not locked this lock");
		}
		lockedThread = null;
		isLocked = false;
		
		if(locks.size() > 0){
			locks.get(0).doNotify();
		}
	}
	
	
	public static void main(String[] args){
		int nthread = 5;
		final CountDownLatch start = new CountDownLatch(1);
		final CountDownLatch end = new CountDownLatch(nthread);
		final FairLock lock = new FairLock();
		
		for(int i = 0 ; i < nthread ; i++){
			new Thread(new Runnable(){
				@Override
				public void run() {	
					try {
						start.await();
						
						System.out.println(this + " try to lock");
						
						lock.lock();
						System.out.println(this + " locked");
						TimeUnit.MILLISECONDS.sleep(600);
						System.out.println(this + " try to unlock");
						lock.unlock();
						System.out.println(this + " unlocked");
						
						end.countDown();
						
					} catch (InterruptedException e) {
						System.out.println("Interrupted");
						return;
					}
				}}).start();
		}
		
		start.countDown();;
		try {
			end.await();
		} catch (InterruptedException e) {
			return;
		}
		
	}
}
