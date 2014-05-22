package yuanye.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

/**
 * Created by Kali on 14-5-6.
 * Copy data from one to another.
 */
public class ChannelCopy {

    public static void main(String... args) throws IOException {

        //using stdin and stdout.
        ReadableByteChannel stdin = Channels.newChannel(System.in);
        WritableByteChannel stdout = Channels.newChannel(System.out);
        channelCopy1(stdin,stdout);

        stdin.close();
        stdout.close();
    }

    public static void channelCopy1(ReadableByteChannel src,WritableByteChannel dest) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(2 * 1024);
        while ( src.read(buffer) != -1){
            //buffer prepare to read
            buffer.flip();

            //write buffer to destination
            dest.write(buffer);

            //if partial transfer,shift the remaining
            //if buffer is empty,same as clear();
            buffer.compact();
        }

        //if buffer has remaining
        buffer.flip();
        while (buffer.hasRemaining()){
            dest.write(buffer);
        }
    }
}
