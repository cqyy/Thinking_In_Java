package cn.yuanye.nio;

import java.io.IOException;
import java.nio.file.*;

/**
 * Created by Kali on 14-4-28.
 */
public class MetadataOperation {
    private static FileSystem fs = FileSystems.getDefault();

    public static void main(String... args) throws IOException {
        getAllSupportedAttributeViews();
        createLinks();
    }

    public static void getAllSupportedAttributeViews(){
        System.out.println("\nGet All Supported Attribute Views"
        +"\n---------------------------------------------------------");
        fs.supportedFileAttributeViews().forEach(System.out::println);
    }

    public static void createLinks() throws IOException {
        Path target = Paths.get("./","README.md");
        Path link = Paths.get("./","links");
        Path finalLink = Paths.get(link.toString(),"README.md");
        Path newLink = Files.createLink(finalLink,target);

        System.out.println("\nCreate a new link" + newLink.toAbsolutePath());
    }
}
