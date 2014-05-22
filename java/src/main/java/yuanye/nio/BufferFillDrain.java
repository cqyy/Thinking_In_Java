package yuanye.nio;

import java.nio.ByteBuffer;

/**
 * A example of buffer fill/drain.
 * This example use the simplest way to do this, one byte a time.
 */
public class BufferFillDrain {

    private static String str[] = {
            "Only miss the sun when it starts to snow",
            "Only know you love her when you let her go",
            "Only know you've been high when you're feeling low",
            "Only hate the road when you’re missin' home",
            "Only know you love her when you let her go"};
    private static int index = 0;

    public static void main(String... args){
        ByteBuffer buffer = ByteBuffer.allocate(512);
        while (fillBuffer(buffer)){
            buffer.flip();
            drainBuffer(buffer);
            buffer.clear();
        }
    }


    public static boolean fillBuffer(ByteBuffer buffer){
       if(index >= str.length){
           return false;
       }

        String string = str[index++];
        for(int i = 0; i < string.length(); i++){
            buffer.put((byte)string.charAt(i));
        }
        return true;
    }

    public static void drainBuffer(ByteBuffer buffer){
        while (buffer.hasRemaining()){
            System.out.print((char) buffer.get());
        }
        System.out.println();
    }
}
