package cn.yuanye.nio;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by Kali on 14-5-2.
 */
public class BasicFileChannel {

    private static String file = "./basicFileChannel.txt";

    private static void writeFile() throws IOException {
        RandomAccessFile randomAccessFile = new RandomAccessFile(file,"rw");
        FileChannel fc = randomAccessFile.getChannel();
        String str = "The snow blows white on the mountain tonight\n" +
                     "Not a footprint to be seen\n" +
                     "A kingdom of isolation and it looks like I'm the Queen";
        char[] chars = str.toCharArray();
        ByteBuffer byteBuffer = ByteBuffer.allocate( chars.length);
        for(char c : chars){
            byteBuffer.put((byte) c);
        }
        byteBuffer.flip();
        fc.write(byteBuffer);
        fc.close();
        randomAccessFile.close();
    }

    private static void readFile() throws IOException {
        RandomAccessFile randomAccessFile = new RandomAccessFile(file,"rw");
        FileChannel fc = randomAccessFile.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(48);
        int index = 0;
        int rlen = 0;
        while ( (rlen = fc.read(byteBuffer,index)) > 0){
            index += rlen;
            byteBuffer.flip();
            while (byteBuffer.hasRemaining()){
                System.out.print((char)byteBuffer.get());
            }
            byteBuffer.clear();
        }
    }


    public static void main(String... args) throws IOException {
        writeFile();
        readFile();
    }

}
