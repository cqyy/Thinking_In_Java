package yuanye.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by Kali on 14-9-1.
 */
public class AsynchronousChannelDemo {

    public static void main(String[] args) {
        SocketAddress address = new InetSocketAddress("www.baidu.com",80);
        AsynchronousSocketChannel channel = null;
        try {
            channel = AsynchronousSocketChannel.open();
            Future<Void> connected = channel.connect(address);
            connected.get();
            System.out.println("connected");
            ByteBuffer buffer = ByteBuffer.allocate(74);
            Future<Integer> future = channel.read(buffer);
            future.get();
            buffer.flip();
            System.out.println(Arrays.toString(buffer.array()));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }
}
