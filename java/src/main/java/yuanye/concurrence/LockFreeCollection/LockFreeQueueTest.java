package yuanye.concurrence.LockFreeCollection;

import org.junit.Test;

import static org.junit.Assert.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Administrator on 14-1-12.
 */
public class LockFreeQueueTest {

    static class Producer implements Runnable {
        private static int count = 0;
        private int id = ++count;
        private CountDownLatch start ;
        private CountDownLatch end;
        private int  n = 0;
        private LockFreeQueue<String> queue ;

        public Producer(CountDownLatch start,CountDownLatch end,int n,LockFreeQueue<String> queue){
            this.start = start;
            this.end = end;
            this.n = n;
            this.queue = queue;
        }


        @Override
        public void run() {
            try {
                start.await();
            } catch (InterruptedException e) {
                return;
            }
            for (int i = 0; i < n; i++) {
                queue.enQueue(id +" : " + i);
            }
            end.countDown();
        }
    }

    static class Consumer implements Runnable {

        private CountDownLatch start ;
        private CountDownLatch end;
        private AtomicInteger count;
        private LockFreeQueue<String> queue ;

        public Consumer(CountDownLatch start,CountDownLatch end,AtomicInteger count,LockFreeQueue<String> queue){
            this.start = start;
            this.end = end;
            this.count = count;
            this.queue = queue;
        }
        @Override
        public void run() {
            try {
                start.await();
            } catch (InterruptedException e) {
                return;
            }

            while (queue.deQueue() != null) {
                count.getAndIncrement();
            }
            end.countDown();
        }
    }

    @Test
    public void deQueuetest() throws InterruptedException {
        final int testTimes = 1000;
        final int nThread = 100;
        final int nProduct = 5000;
        CountDownLatch start;
        CountDownLatch end;
        LockFreeQueue<String> queue = new LockFreeQueue<String>();
        AtomicInteger count = new AtomicInteger(0);

        for (int t = 0; t < testTimes;  t++) {
            //init the product
            for(int i = 0 ; i < nProduct ; i ++ ){
                queue.enQueue(i +"");
            }
            count.set(0);
            start = new CountDownLatch(1);
            end = new CountDownLatch(nThread);

            for(int i = 0; i < nThread ;i++){
                new Thread(new Consumer(start,end,count,queue)).start();
            }
            start.countDown();
            end.await();

            if(nProduct != count.get()){
                fail("should be " + nProduct + " actual is " + count.get());
            }
        }
    }

    @Test
    public void enQueuetest() {
        final int testTimes = 1000;
        final int nThread = 10;
        final int nP = 500;
        CountDownLatch start;
        CountDownLatch end;
        LockFreeQueue<String> queue = new LockFreeQueue<String>();

        for (int t = 0; t < testTimes;  t++) {

            while (queue.deQueue() != null){}; //// clear the queue
            start = new CountDownLatch(1);
            end = new CountDownLatch(nThread);

            for (int i = 0; i < nThread; i++) {
                new Thread(new Producer(start,end,nP,queue)).start();
            }
            start.countDown();
            try {
                end.await();
            } catch (InterruptedException e) {
                return;
            }

            if (queue.size() != nThread * nP) {
                fail("times " + t + " should be " + nThread * nP + " but actual is  " + queue.size());
            }
        }

    }

    @Test
    public void integratedtest(){
        final int nProducer = 20;
        final int nCosumer = 20;
        final int testTimes = 1000;
        final int productsPerProducer = 500;
        final AtomicInteger count = new AtomicInteger(0);
        LockFreeQueue<String> queue = new LockFreeQueue<String>();
        CountDownLatch start;
        CountDownLatch end;


        for(int i = 0 ; i < testTimes ; i++){
            count.set(0);
            while(queue.deQueue() != null){} //clear the queue
            start = new CountDownLatch(1);
            end = new CountDownLatch(nCosumer + nProducer);

            for(int j = 0 ; j < nCosumer ; j++){
                new Thread(new Consumer(start,end,count,queue)).start();
            }

            for(int j = 0 ; j < nProducer ; j++){
                new Thread(new Producer(start,end,productsPerProducer,queue)).start();
            }

            start.countDown();
            try {
                end.await();
            } catch (InterruptedException e) {  }

            if(count.get() != nProducer * productsPerProducer - queue.size()){
                fail("count.get()= " + count.get() + " queue.size()= " + queue.size());
            }

        }


    }
}
