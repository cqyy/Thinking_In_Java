package cn.yuanye.nio;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.GatheringByteChannel;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Created by Kali on 14-5-6.
 */
public class GatheringWrite {
    private static String filename = "gathing.txt";

    public static void main(String... args) throws IOException {
        FileOutputStream stream = new FileOutputStream(new File((filename)));
        GatheringByteChannel channel = stream.getChannel();

        ByteBuffer[] buffers = randomBuffers(10);

        //Gathering
        while (channel.write(buffers) > 0){
            //do nothing
        }
        System.out.println("done");
        channel.close();
        stream.close();
    }


    // ------------------------------------------------
    // These are just representative; add your own
    private static String [] col1 = {
            "Aggregate", "Enable", "Leverage",
            "Facilitate", "Synergize", "Repurpose",
            "Strategize", "Reinvent", "Harness"
    };
    private static String [] col2 = {
            "cross-platform", "best-of-breed", "frictionless",
            "ubiquitous", "extensible", "compelling",
            "mission-critical", "collaborative", "integrated"
    };
    private static String [] col3 = {
            "methodologies", "infomediaries", "platforms",
            "schemas", "mindshare", "paradigms",
            "functionalities", "web services", "infrastructures"
    };
    private static String newline = System.getProperty ("line.separator");
    private static Random random = new Random();

    private static ByteBuffer[] randomBuffers(int num){
        List<ByteBuffer> bufferList = new LinkedList<>();

        for(int i = 0; i < num; i++){
            bufferList.add(pickRandom(col1," "));
            bufferList.add(pickRandom(col2," "));
            bufferList.add(pickRandom(col3,newline));
        }
        ByteBuffer[] buffers = new ByteBuffer[bufferList.size()];
        bufferList.toArray(buffers);

        return buffers;
    }

    private static ByteBuffer pickRandom(String[] strings,String suffix){
        String str = strings[random.nextInt(strings.length)];
        int total = str.length() + suffix.length();
        ByteBuffer buffer = ByteBuffer.allocate(total);

        buffer.put(str.getBytes());
        buffer.put(suffix.getBytes());
        buffer.flip();
        return buffer;
    }
}
