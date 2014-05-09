package cn.yuanye.nio;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * Created by Kali on 14-5-8.
 * Example of channel transform.
 * This example transform from FileChannel to a SocketChannel
 */
public class ChannelTransform {
    private static final int PORT = 4096;

    private static class ChannelReadThread extends Thread{

        private final ServerSocketChannel serverSocketChannel;

        private ChannelReadThread() throws IOException {
            this.serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.socket().bind(new InetSocketAddress(PORT));
        }

        private void listenOnPort() throws IOException {
            SocketChannel channel = serverSocketChannel.accept();
            ByteBuffer buffer = ByteBuffer.allocate(1024);

            while ( channel.read(buffer) > 0){
                buffer.flip();
                while (buffer.hasRemaining()){
                    System.out.print((char) buffer.get());
                }
                buffer.clear();
            }
        }

        @Override
        public void run() {
            try {
                this.listenOnPort();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        new ChannelReadThread().start();

        File file = new File("D:/lrc.txt");
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress(PORT));
        RandomAccessFile randomAccessFile = new RandomAccessFile(file,"r");
        FileChannel fileChannel = randomAccessFile.getChannel();
        fileChannel.transferTo(0,fileChannel.size(),socketChannel);
        fileChannel.close();
        socketChannel.close();
    }
}
