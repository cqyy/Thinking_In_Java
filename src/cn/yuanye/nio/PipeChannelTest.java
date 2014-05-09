package cn.yuanye.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.Pipe;
import java.nio.channels.WritableByteChannel;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by Kali on 14-5-9.
 */
public class PipeChannelTest {


    private static class Worker extends Thread {

        private final WritableByteChannel sink;
        private final String[] msgs = new String[]{
                "You can either travel or read,and either your body or soul must be on the way.",
                "It is not easy to meet each other in such a big world.",
                "Sometimes you must let it go ,to see if there was anything worth holding on to.",
                "No one indebted for others,while many people don't know how to cherish others. ",
                "Within you I lose myself, without you I find myself wanting to be lost again. "};
        private final Random random = new Random();
        private final int repes ;

        private Worker(WritableByteChannel sink,int repes) {
            this.sink = sink;
            this.repes = repes;
        }

        private void work() throws IOException {
            String str = msgs[random.nextInt(msgs.length)];
            ByteBuffer buffer = ByteBuffer.allocate(str.length());
            buffer.put(str.getBytes());
            buffer.flip();

            sink.write(buffer);

            //write all the data
            while ( sink.write(buffer) > 0);

        }

        @Override
        public void run() {
            for(int i = 0; i < repes; i++){
                try {
                    work();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            try {
                sink.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static void main(String... args) throws IOException {
        Pipe pipe = Pipe.open();
        WritableByteChannel stdout = Channels.newChannel(System.out);
        Worker worker = new Worker(pipe.sink(),10);
        worker.start();

        ByteBuffer buffer = ByteBuffer.allocate(512);

        while (pipe.source().read(buffer) > 0){
            buffer.flip();
            stdout.write(buffer);
            buffer.clear();
            System.out.println();
        }
    }


}
