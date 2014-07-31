package yuanye.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.EnumSet;
import java.util.concurrent.TimeUnit;

/**
 * Created by Kali on 14-9-1.
 */
public class FileLockDemo {

    public static void main(String[] args) {
        Path path = Paths.get("D:\\test.txt");
        FileLock lock = null;
        try (FileChannel channel = FileChannel.open(path,
                EnumSet.of(StandardOpenOption.CREATE, StandardOpenOption.WRITE))) {
            ByteBuffer buffer = ByteBuffer.wrap("Hello Word".getBytes());
            channel.write(buffer);
            lock = channel.lock();
            System.out.println("is lock shared " + lock.isShared());
            TimeUnit.SECONDS.sleep(10);

        } catch (IOException ie) {
            ie.printStackTrace();
            ;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            path.toFile().delete();
            if (lock != null) {
                try {
                    lock.release();
                } catch (IOException e) {
                }
            }
        }
    }
}
