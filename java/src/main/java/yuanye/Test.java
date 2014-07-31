package yuanye;

import java.nio.CharBuffer;

/**
 * Created by Administrator on 2014/8/19.
 */
public class Test {

    public static void main(String[] args) {

        CharBuffer buffer = CharBuffer.wrap("Hello World");
        System.out.println(buffer);
        buffer.position(6);
        CharBuffer buffer2 = buffer.slice();
        System.out.println(buffer2);
        System.out.println(buffer2.capacity());
    }
}
