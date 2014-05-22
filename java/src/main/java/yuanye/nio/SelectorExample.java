package yuanye.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by Kali on 14-5-1.
 * A simple demo of Selector of Selector in Java NIO.
 * Using selector deal both new connection creation and data reading.
 */
public class SelectorExample {

    private static class ServerListener extends Thread{
        private final ServerSocketChannel serverSocketChannel;
        private final Selector selector;
        private final int port = 4096;

        private ServerListener() throws IOException {
            selector = Selector.open();

            //init listening port
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);                     //set blocking to false,selector required
            serverSocketChannel.bind(new InetSocketAddress(port));
            serverSocketChannel.register(selector,SelectionKey.OP_ACCEPT);    //register to selector for interest of ACCEPT
        }

        //listen the selector
        private void startListener() throws IOException {
            //deal connection
            while (true){
                if (selector.select() == 0){
                    continue;
                }
                Set<SelectionKey> keys =  selector.selectedKeys();
                Iterator<SelectionKey> iterator = keys.iterator();
                while (iterator.hasNext()){
                    SelectionKey key = iterator.next();

                    //ServerSocketChannel
                    if (key.isAcceptable()){
                        ServerSocketChannel channel = (ServerSocketChannel)key.channel();
                        SocketChannel con = channel.accept();                  //accept new connection
                        con.configureBlocking(false);
                        con.register(selector,SelectionKey.OP_READ);           //register to selector
                    }

                    //SocketChannel
                    else  if (key.isReadable()){
                        handleSocketChannel(key);
                    }
                    iterator.remove();
                }
            }
        }

        //handle socket channel,read data from the channel and display.
        private void handleSocketChannel(SelectionKey key){
            SocketChannel channel = (SocketChannel)key.channel();
            ByteBuffer buffer = ByteBuffer.allocate(256);
            try {
                while (channel.read(buffer) > 0){
                    buffer.flip();                               //prepare to be read
                    while (buffer.hasRemaining()){
                        System.out.print((char) buffer.get());
                    }
                    buffer.clear();                             //prepare to write in
                }
                System.out.println();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                startListener();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static class Client extends Thread{
        private static Random random = new Random();
        private static int count = 0;

        private final int interval = random.nextInt(5);
        private final int id = ++count;

        @Override
        public void run() {
            try {
                SocketChannel channel = SocketChannel.open(new InetSocketAddress(4096));
                ByteBuffer byteBuffer = ByteBuffer.allocate(64);
                char[] msg = ("Hello from " + this.toString()).toCharArray();
                for(char c : msg){
                    byteBuffer.put((byte)c);
                }
                while (true){
                    byteBuffer.flip();
                    channel.write(byteBuffer);
                    TimeUnit.SECONDS.sleep(interval);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        @Override
        public String toString() {
            return "Client_" + id;
        }
    }


    public static void main(String[] args) throws IOException {
        ServerListener listener = new ServerListener();
        listener.start();
        try {
            TimeUnit.MILLISECONDS.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for(int i = 0; i < 3; i++){
            new Client().start();
        }

    }
}
