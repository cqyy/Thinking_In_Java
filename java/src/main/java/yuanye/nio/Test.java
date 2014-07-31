package yuanye.nio;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.TimeUnit;

/**
 * Created by Kali on 14-8-10.
 */
public class Test {

    private static class Server extends Thread{
        private final ServerSocketChannel serverSocketChannel;
        private final InetSocketAddress address;

        public Server(InetSocketAddress address) throws IOException {
            this.address = address;
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(address);
        }

        @Override
        public void run() {
            try {
                SocketChannel channel = serverSocketChannel.accept();
                ByteBuffer data = ByteBuffer.allocate(4);
                data.putInt(1);
                data.flip();
                channel.write(data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static class Client extends Thread{
        private final InetSocketAddress serverAddr;
        private final Socket socket;

        public Client(InetSocketAddress address) throws IOException {
            this.serverAddr = address;
            socket = new Socket();
            socket.connect(serverAddr);
        }

        @Override
        public void run() {
            try {
                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                int data = dataInputStream.readInt();
                System.out.println(data);
                System.out.println(socket.getChannel());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        InetSocketAddress address = new InetSocketAddress("localhost",9001);
        Server server = new Server(address);
        server.start();
        TimeUnit.MILLISECONDS.sleep(200);
        Client client = new Client(address);
        client.start();
    }
}
