package yuanye.network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.TimeUnit;

/**
 * Created by Kali on 14-8-27.
 */
public class SO_LINGER_Test {

    private static class Server implements Runnable {
        //private final InetSocketAddress address;
        private ServerSocketChannel serverSocketChannel;

        public Server(InetSocketAddress address){
            //this.address = address;
            try{
                serverSocketChannel  = ServerSocketChannel.open();
                serverSocketChannel.bind(address);
            }catch (IOException ie){
                ie.printStackTrace();
            }
        }


        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()){
                try {
                    SocketChannel channel = serverSocketChannel.accept();
                    channel.socket().setReceiveBufferSize(1024*12);
                    /**
                     * Just accept a connection,and don't read data from it.
                     */
                } catch (IOException ignored) {
                    //ignored
                }
            }
        }
    }

    private static void blockTest(InetSocketAddress address){
        try{
            SocketChannel socketChannel = SocketChannel.open();
            socketChannel.socket().setSendBufferSize(1024);
            socketChannel.socket().setSoLinger(true,10);          //wait for 10 secs
            socketChannel.connect(address);
            ByteBuffer buffer = ByteBuffer.allocate(1024 * 12);        //bigger than send buffer
            for(int i = 0; i < buffer.capacity(); i++){
                //fill in buffer
                buffer.put((byte) i);
            }
            buffer.flip();
            System.out.println("Writing data to server");
            socketChannel.write(buffer);
            System.out.println("Closing socket");
            socketChannel.close();
            System.out.println("Socket is closed");
        }catch (IOException ie){
            ie.printStackTrace();
        }
    }

    private static void unblockTest(InetSocketAddress address){
        try{
            SocketChannel socketChannel = SocketChannel.open();
            socketChannel.socket().setSendBufferSize(1024);
            socketChannel.socket().setSoLinger(true, 10);          //wait for 10 secs
            socketChannel.configureBlocking(false);                //change to unblocking mode
            socketChannel.connect(address);
            ByteBuffer buffer = ByteBuffer.allocate(1024 * 12);        //bigger than send buffer
            for(int i = 0; i < buffer.capacity(); i++){
                //fill in buffer
                buffer.put((byte) i);
            }
            buffer.flip();
            while (!socketChannel.finishConnect()){
                try {
                    TimeUnit.MILLISECONDS.sleep(200);
                } catch (InterruptedException e) {
                    return;
                }
            }
            System.out.println("Writing data to server");
            socketChannel.write(buffer);
            System.out.println("Closing socket");
            try {
                socketChannel.close();
            }catch (IOException ie){
                try {
                    TimeUnit.MILLISECONDS.sleep(2000);
                } catch (InterruptedException e) {
                    return;
                }
            }
            System.out.println("Socket is closed");
        }catch (IOException ie){
            ie.printStackTrace();
        }
    }

    private static void timeoutTest(InetSocketAddress address){
        try {
            Socket socket = new Socket();
            socket.setSoTimeout(2000);        //2 secs
            socket.connect(address);
            socket.getInputStream().read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        InetSocketAddress address = new InetSocketAddress("localhost",9998);
        Server server = new Server(address);
        Thread serverTh = new Thread(server);
        serverTh.start();
        try{
            timeoutTest(address);
        }finally {

        serverTh.interrupt();
        }
    }
}
