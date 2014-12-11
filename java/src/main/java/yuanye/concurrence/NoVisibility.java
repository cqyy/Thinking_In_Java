package yuanye.concurrence;

import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 14-3-18.
 */
public class NoVisibility {
    private static volatile boolean ready = false;
    private static int number;

    private static class Thread1 extends Thread{
        @Override
        public void run() {
            int i = 0;
            System.out.println(number);
            while (!ready){
               i++;
            }
            System.out.println(number);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread1 thread1 = new Thread1();
        thread1.start();
        number = 10;
        TimeUnit.MILLISECONDS.sleep(10);
        ready = true;

    }
}
