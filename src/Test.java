import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by Administrator on 14-3-5.
 */

public class Test {
    public static void main(String[] args) {
        FileSystem fs = FileSystems.getDefault();
        Path path = Paths.get("./");
    }
}