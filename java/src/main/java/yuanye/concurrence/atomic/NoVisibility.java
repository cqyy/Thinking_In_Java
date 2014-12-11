package yuanye.concurrence.atomic;

public class NoVisibility {
	private static int nummber;
	private static boolean ready;
	
	
	private static class ReadyThread extends Thread{
		public void run(){
			while(!ready)
				Thread.yield();
		
			System.out.println(nummber);
		}
	}
	
	public static void main(String[] args){
		new ReadyThread().start();
		
		ready = true;
		nummber = 12;
	}
}
