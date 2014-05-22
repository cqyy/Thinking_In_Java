package yuanye.concurrence.shedule;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class HeartBeat implements Runnable{
	private static Random rand = new Random(52);
	
	@Override
	public void run(){
		int period = rand.nextInt(10);
		System.out.println("Bom...... sleep for " + period);
		try {
			TimeUnit.SECONDS.sleep(period);
			System.out.println("Wakeup ...");
		} catch (InterruptedException e) {
			System.out.println("Interrupted");
			return;
		}
	}
	
	public static void main(String[] args) throws InterruptedException{
		ScheduledExecutorService exec = Executors.newScheduledThreadPool(1);
		
		final ScheduledFuture<?> future = exec.scheduleWithFixedDelay(
				new HeartBeat(), 0, 5, TimeUnit.SECONDS);
		
		exec.schedule(new Runnable(){
			@Override
			public void run(){
				future.cancel(true);
				}
		}, 10, TimeUnit.SECONDS);
		TimeUnit.SECONDS.sleep(11);
		//exec.shutdown();
		System.out.println(exec.isTerminated());
	}
}
