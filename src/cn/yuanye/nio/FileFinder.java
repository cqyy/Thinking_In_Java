package cn.yuanye.nio;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * Created by Kali on 14-4-28.
 */
public class FileFinder implements FileVisitor<Path>{

    private final String fileName;
    private boolean founded = false;

    public FileFinder(String fileName) {
        this.fileName = fileName;
    }


    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        System.out.println("finding in -->" + dir.toAbsolutePath());
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        if (file.getFileName().toString().equals(fileName)){
            System.out.println("\n-------------------------------------------------------------"
                    + "\nfounded in --> " + file.toAbsolutePath());
            founded = true;
            return FileVisitResult.TERMINATE;
        }
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
        return FileVisitResult.CONTINUE;
    }

    public static void main(String... args) throws IOException {
        final String filename = "FileFinder.java";
        final Path startPath = Paths.get("./src");
        Files.walkFileTree(startPath,new FileFinder(filename));
    }
}
