package yuanye.rpc;

import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by Kali on 14-7-27.
 */
public class RPC {

    private static class Server extends Thread{
        private InetSocketAddress address;
        private ServerSocketChannel channel;
        private Selector selector;
        private Object instance;
        private Reader[] readers;
        private Executor readerExecutor;

        public Server(InetSocketAddress address,Object instance,int num){
            this.address = address;
            this.instance = instance;
            if(num <= 0){num = 1;}
            readers = new Reader[num];
            readerExecutor = Executors.newFixedThreadPool(num);
        }

        @Override
        public synchronized void start() {

            super.start();
        }

        private class Reader implements Runnable {
            private volatile boolean shouldRun = true;
            private Selector selector;

            public Reader(Selector selector){
                this.selector = selector;
            }

            public SelectionKey registerChannel(SelectableChannel channel) throws ClosedChannelException {
                return  channel.register(selector,SelectionKey.OP_READ);
            }

            @Override
            public void run() {

            }

            public void stop(){

            }
        }

    }

    private class Client{}
}
