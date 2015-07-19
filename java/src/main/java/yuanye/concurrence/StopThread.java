package yuanye.concurrence;

import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 14-3-12.
 */
public class StopThread {
    private static volatile boolean stop = false;  //add 'volatile' to repair the problem


    public static void main(String[] args) throws InterruptedException {
        Thread backgroundThread = new Thread(new Runnable() {
            @Override
            public void run() {
                int i = 0;
                while (!stop){
                    i++;
                }
            }
        });
        backgroundThread.start();

        TimeUnit.SECONDS.sleep(1);
        stop = true;
    }
}
