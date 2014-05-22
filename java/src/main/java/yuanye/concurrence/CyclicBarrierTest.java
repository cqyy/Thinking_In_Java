package yuanye.concurrence;

import java.util.Random;
import java.util.concurrent.*;

/**
 * Created by Administrator on 14-3-25.
 */
public class CyclicBarrierTest {
    private final int nworkers;
    private final CyclicBarrier barrier;
    private final Worker[] workers;
    private final Executor executor;

    public CyclicBarrierTest(int nworkers) {
        if (nworkers < 1) {
            throw new IllegalArgumentException("ivalid nworkers: " + nworkers);
        }
        this.nworkers = nworkers;
        this.barrier = new CyclicBarrier(nworkers, new Runnable() {
            @Override
            public void run() {
                System.out.println("work completed!");
            }
        });
        workers = new Worker[nworkers];
        for (int i = 0; i < nworkers; i++) {
            workers[i] = new Worker(barrier);
        }
        executor = Executors.newFixedThreadPool(nworkers);
    }

    public void start() {
        for (Worker worker : workers) {
            executor.execute(worker);
        }
    }

    static class Worker implements Runnable {
        private static int count = 0;
        private static Random random = new Random(47);

        private final int id = ++count;
        private final int workTime = random.nextInt(10);
        private final CyclicBarrier barrier;
        private final int nworkPhase = 2;

        public Worker(CyclicBarrier barrier) {
            this.barrier = barrier;
        }

        @Override
        public void run() {
            for (int i = 1; i <= nworkPhase; i++) {
                System.out.println(this.toString() + " start to work. phase " + i);
                //work
                try {
                    TimeUnit.SECONDS.sleep(workTime);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                System.out.println(this.toString() + " work completed for time :" + workTime);
                try {
                    barrier.await();
                } catch (InterruptedException e) {
                    return;
                } catch (BrokenBarrierException e) {
                    return;
                }
            }
        }

        @Override
        public String toString() {
            return "worker--" + id;
        }
    }

    public static void main(String[] args) {
        CyclicBarrierTest test = new CyclicBarrierTest(5);
        test.start();
    }

}
