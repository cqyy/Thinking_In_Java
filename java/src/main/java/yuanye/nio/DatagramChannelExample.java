package yuanye.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.concurrent.TimeUnit;

/**
 * Created by Kali on 14-5-8.
 * This example illustrate the basic use of DatagramChannel by a time server and client.
 */
public class DatagramChannelExample {

    private static final int PORT = 4098;

    private static class EchoServer extends Thread{
        private final DatagramChannel channel;


        private EchoServer() throws IOException {
            this.channel = DatagramChannel.open();
            channel.socket().bind(new InetSocketAddress(PORT));
            System.out.println("Server started.");
        }

        @Override
        public void run() {
            ByteBuffer buffer = ByteBuffer.allocate(512);
            while (!Thread.currentThread().isInterrupted()){
                buffer.clear();
                try {
                    SocketAddress address = channel.receive(buffer);
                    if (address == null){
                        continue;
                    }
                    buffer.flip();

                    StringBuilder sb = new StringBuilder();
                    while (buffer.hasRemaining()){
                        sb.append((char)buffer.get());
                    }

                    System.out.format("Get connection from %s,msg: %s \n",address.toString(),sb.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static void main(String... args) throws IOException {
        EchoServer server = new EchoServer();
        server.start();

        DatagramChannel channel = DatagramChannel.open();
        ByteBuffer buffer = ByteBuffer.allocate(512);
        buffer.put("Hello".getBytes());
        buffer.flip();
        buffer.mark();
        SocketAddress address = new InetSocketAddress("127.0.0.1",PORT);

        for(int i = 0; i < 5; i++){

            channel.send(buffer,address);
            buffer.reset();
            try {
                TimeUnit.MILLISECONDS.sleep(500 );
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
