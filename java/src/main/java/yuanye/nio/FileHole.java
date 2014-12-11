package yuanye.nio;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by Kali on 14-5-7.
 * Create a file with file holes
 */
public class FileHole {
    public static void main(String... args) throws IOException {
        File temp = File.createTempFile("temp", null);
        RandomAccessFile randomAccessFile = new RandomAccessFile(temp,"rw");
        FileChannel channel = randomAccessFile.getChannel();

        ByteBuffer buffer = ByteBuffer.allocate(512);

        write(500,buffer,channel);
        write(1000,buffer,channel);
        write(2*1024*1024,buffer,channel);

        System.out.println ("Wrote temp file '" + temp.getPath( )
                + "', size=" + channel.size( ));

        channel.close();
    }


    public static void write(int position, ByteBuffer buffer, FileChannel channel) throws IOException {
        String str = "*<=postion " + position;
        buffer.put(str.getBytes());

        buffer.flip();
        channel.position(position);
        channel.write(buffer);
        buffer.clear();

    }
}
