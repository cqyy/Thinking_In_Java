package cn.yuanye.concurrence.shedule;

import java.util.concurrent.TimeUnit;

public class InterruptThread {
	
	public static class MyThread extends Thread{
		
		@Override
		public void run(){
			while(true);
//			System.out.println("exit");
//			return;
		}
	}
	
	
	public static void main(String[] args) throws InterruptedException{
		MyThread t = new MyThread();
		t.start();
		TimeUnit.MILLISECONDS.sleep(100);
		t.interrupt();
	}
}
