package yuanye.datastructure;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Administrator on 14-4-26.
 */
public class SpinLock {

    private AtomicInteger lock = new AtomicInteger(0);

    public void spinLock(){
        while (!lock.compareAndSet(0,1));
    }

    public void spinUnlock(){
        lock.compareAndSet(1,0);
    }




    public static void main(String[] args){
        SpinLock lock = new SpinLock();

        for(int i = 0; i < 5; i++){
            new Thread(new TestThread(lock)).start();
        }
    }
}

class TestThread implements Runnable{
    private static int count = 0;
    private final SpinLock lock;
    private final String name = "thread_" + count++;

    TestThread(SpinLock lock) {
        this.lock = lock;
    }

    @Override
    public void run() {
        System.out.println(this.toString() + " is trying to get lock");
        lock.spinLock();
        System.out.println(this.toString() + " holding the lock");
          try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                //
            }
        lock.spinUnlock();

    }

    @Override
    public String toString() {
        return name;
    }
}

