package yuanye.datastructure;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Kali on 14-4-27.
 */
public class SeqLock {

    private final AtomicInteger seq = new AtomicInteger(0);
    private final SpinLock spinLock = new SpinLock();

    public int readSeqBegin() {
        return seq.get();
    }

    public boolean readSeqRetry(int lock) {
        return (seq.get() == lock)&&( lock%2 != 1);
    }

    public void writeSeqLock() {
        spinLock.spinLock();
        seq.getAndIncrement();
    }

    public void writeSeqUnlock() {
        seq.getAndIncrement();
        spinLock.spinUnlock();
    }

    public static void main(String[] args)  {
        SeqLock lock = new SeqLock();

        //reader
        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                int seq = 0;
                do {
                    seq = lock.readSeqBegin();
                    try {
                        TimeUnit.MILLISECONDS.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().toString() + " read info");
                } while (!lock.readSeqRetry(seq));
                System.out.println(Thread.currentThread().toString() + " read info valid");
            }).start();
        }


        //writer
        for(int i = 0; i < 2; i++){
            new Thread(()->{
                lock.writeSeqLock();
                System.out.println(Thread.currentThread().toString() + " get the write lock");
                try {
                    TimeUnit.MILLISECONDS.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().toString() + " write completed");
                lock.writeSeqUnlock();

            }
            ).start();
        }
    }
}

/*class Time {
    long time;
}

class SeqLockTestTheadReader extends Thread {
    private static int count = 0;
    private final Time time;
    private final SeqLock lock;
    private final int id = count++;

    SeqLockTestTheadReader(Time time, SeqLock lock) {
        this.time = time;
        this.lock = lock;
    }

    @Override
    public void run() {
        int seq = 0;
        do {
            seq = lock.readSeqBegin();
            System.out.println(this + " " + time.time);
        } while (lock.readSeqRetry(seq));
    }

    @Override
    public String toString() {
        return "thread_" + id;
    }
}*/


