<<<<<<< HEAD
=======
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

>>>>>>> f5fec297f03e129fa90b37ee03e7aa1c0af05e98
public class JoyOfHex {
    private static final int _1MB = 1024 * 1024;

    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(12);
        System.out.println(ByteOrder.nativeOrder());
    }
}