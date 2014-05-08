import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class JoyOfHex {
    private static final int _1MB = 1024 * 1024;

    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(12);
        System.out.println(ByteOrder.nativeOrder());
    }
}