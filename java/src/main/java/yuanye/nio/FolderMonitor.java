package yuanye.nio;

import java.io.IOException;
import java.nio.file.*;

/**
 * Created by Kali on 14-4-28.
 * Monitor a folder,if there is any changed in the folder,print changes out
 */
public class FolderMonitor {

    public static void minitor(Path path) {
        WatchService ws = null;
        try {
            ws = FileSystems.getDefault().newWatchService();
            path.register(ws,
                    StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_DELETE,
                    StandardWatchEventKinds.ENTRY_MODIFY);
            while (true){
                WatchKey watchKey = ws.take();
                watchKey.pollEvents().forEach((action)->{
                    final WatchEvent.Kind<?> kind = action.kind();
                    if (kind != StandardWatchEventKinds.OVERFLOW){
                        System.out.println(kind + " ---> " + ((WatchEvent<Path>)action).context().toAbsolutePath());
                    }
                });
                if (!watchKey.reset()){
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }

    public static void main(String... args){
        Path path = Paths.get("./links");
        minitor(path);
    }
}
