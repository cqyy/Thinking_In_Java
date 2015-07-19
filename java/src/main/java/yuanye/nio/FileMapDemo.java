package yuanye.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.EnumSet;

/**
 * Created by Kali on 14-9-1.
 */
public class FileMapDemo {

    private static void printBuffer(ByteBuffer buffer){
        while (buffer.hasRemaining()){
            System.out.print((char)buffer.get());
        }
        System.out.println();
    }

    public static void main(String[] args) {
        Path path = Paths.get("D:","test.txt");
        try(FileChannel channel = FileChannel.open(path,EnumSet.of(
                StandardOpenOption.CREATE,
                StandardOpenOption.WRITE,
                StandardOpenOption.READ))){
            ByteBuffer byteBuffer = ByteBuffer.wrap("12345678".getBytes());
            channel.write(byteBuffer);
            MappedByteBuffer mappedByteBuffer = channel.map(FileChannel.MapMode.READ_WRITE,0,channel.size());

            ByteBuffer readBuf = ByteBuffer.allocate(32);
            System.out.println("Read from mapped buffer");
            printBuffer(mappedByteBuffer);

            mappedByteBuffer.position(2);
            mappedByteBuffer.put((byte)'0');

            System.out.println("Before force");
            channel.position(0);
            byteBuffer.clear();
            channel.read(readBuf);
            readBuf.flip();
            printBuffer(readBuf);

            System.out.println("After force");
            mappedByteBuffer.force();
            byteBuffer.clear();
            channel.position(0);
            channel.read(readBuf);
            readBuf.flip();
            printBuffer(readBuf);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(!path.toFile().delete())
                System.out.println("Delete file " + path + " failed");
        }
    }
}
