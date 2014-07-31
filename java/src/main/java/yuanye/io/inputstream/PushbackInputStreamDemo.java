package yuanye.io.inputstream;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PushbackInputStream;
import java.util.Arrays;

/**
 * Created by Kali on 14-8-31.
 * PusbackInputStream extends FilterInputStream,is intended for situations where
 * you wanna check what is to read and then read it again.
 * It provide new method 'unread()' to push back byte you read.Buffer size of
 * this filter input stream is 1 byte default.Also you could change it when construct it
 * by its constructor.
 */
public class PushbackInputStreamDemo {

    public static void main(String[] args) {
        PushbackInputStream in = new PushbackInputStream(
                new ByteArrayInputStream(
                        "123456".getBytes()),2);        //init the buffer size to 2 bytes

        try {
            byte b1 = (byte)in.read();
            byte b2 = (byte)in.read();
            System.out.println((char)b1);
            System.out.println((char)b2);
            in.unread(b1);
            in.unread(b2);
            System.out.println((char)in.read());
            System.out.println((char)in.read());


            byte bytes[] = new byte[2];
            in.read(bytes);
            System.out.println(Arrays.toString(bytes));
            in.unread(bytes);
            in.read(bytes);
            System.out.println(Arrays.toString(bytes));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
