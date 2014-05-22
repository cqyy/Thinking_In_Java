package yuanye.nio;

import java.nio.CharBuffer;

/**
 * Created by Kali on 14-5-6.
 * This example focus on creation of buffer.
 * This example use CharBuffer to illustrate the method of creation.
 */
public class BufferCreation {
    public static void main(String... args){
        //allocate
        //directly allocate a buffer to hold 1024 char object.
        CharBuffer buffer1 = CharBuffer.allocate(1024);

        //wrap
        //wrap will not create a space to hold the elements, the buffer will just use the array object
        //to hold the elements,so the change is visible to both the array and the buffer
        char[] str = "Buffer".toCharArray();
        CharBuffer buffer2 = CharBuffer.wrap(str);
        System.out.println(buffer2);
        str[0] = 'b';                           //change the array object
        System.out.println(buffer2);

        //wrap with offset will just affects the initial state of the buffer object.
        char[] str2 = "Hello Buffer".toCharArray();
        CharBuffer buffer3 = CharBuffer.wrap(str2,6,6);
        System.out.println(buffer3);
        buffer3.clear();
        System.out.println(buffer3);

    }
}
