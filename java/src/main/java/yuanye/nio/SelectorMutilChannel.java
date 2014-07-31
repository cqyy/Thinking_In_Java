package yuanye.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Kali on 14-8-10.
 */
public class SelectorMutilChannel {

    private static class Server extends Thread {
        private final ServerSocketChannel serverSocketChannel;
        private final Selector selector;
        private final Reader reader;

        public Server(InetSocketAddress address) throws IOException {
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(address);
            selector = Selector.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            reader = new Reader(Selector.open());
        }

        @Override
        public synchronized void start() {
            super.start();
            reader.start();
        }

        @Override
        public void run() {
            while (!isInterrupted()) {
                try {
                    selector.select();
                    Set<SelectionKey> keySet = selector.selectedKeys();
                    Iterator<SelectionKey> iterator = keySet.iterator();
                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();
                        SocketChannel channel = ((ServerSocketChannel)key.channel()).accept();
                        channel.configureBlocking(false);
                        reader.startAdd();
                        reader.registerChannel(channel);
                    }
                } catch (IOException e) {
                    try {
                        serverSocketChannel.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    Thread.currentThread().interrupt();
                }finally {
                    reader.finishedAdd();
                }
            }
        }

        private class Reader extends Thread {
            private final Selector selector;
            private volatile boolean adding;

            public void startAdd(){
                adding = true;
                selector.wakeup();
            }

            public void finishedAdd(){
                adding = false;
                synchronized (this){
                this.notifyAll();}
            }

            public Reader(Selector selector) {
                this.selector = selector;
            }

            public SelectionKey registerChannel(SelectableChannel channel) throws ClosedChannelException {
                return channel.register(selector, SelectionKey.OP_READ);
            }

            @Override
            public void run() {
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        selector.select();
                        while (adding){
                            synchronized (this){
                                try {
                                    this.wait(100);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        Set<SelectionKey> keySet = selector.selectedKeys();
                        Iterator<SelectionKey> iterator = keySet.iterator();
                        while (iterator.hasNext()) {
                            SelectionKey key = iterator.next();
                            SocketChannel channel = (SocketChannel) key.channel();
                            doRead(channel);
                        }
                    } catch (IOException e) {
                        try {
                            selector.close();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        Thread.currentThread().interrupt();
                    }
                }
            }

            private void doRead(SocketChannel channel) {
                ByteBuffer buffer = ByteBuffer.allocate(4);
                try {
                    channel.read(buffer);
                    buffer.flip();
                    System.out.println("Reading data from channel " + buffer.getInt());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static class Client extends Thread{
        private final SocketChannel channel;
        private static final AtomicInteger IDGEN = new AtomicInteger();
        private final int id = IDGEN.getAndIncrement();
        public Client(InetSocketAddress address) throws IOException {
            channel = SocketChannel.open();
            channel.connect(address);
        }

        private void sendData(int id) throws IOException {
            ByteBuffer buffer = ByteBuffer.allocate(4);
            buffer.putInt(id);
            buffer.flip();
            channel.write(buffer);
        }

        @Override
        public void run() {
            while (true){

                try {
                    sendData(id);
                    System.out.println("Sended data");
                } catch (IOException e) {
                    e.printStackTrace();
                }                try {
                    TimeUnit.MILLISECONDS.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        InetSocketAddress address = new InetSocketAddress("localhost",8089);
        Server server = new Server(address);
        server.start();

        Client client1 = new Client(address);
        client1.start();
        //client1.sendData(1);
        Client client2 = new Client(address);
        client2.start();
    }
}
