package yuanye.nio;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * Created by Administrator on 2014/7/30.
 */
public class CodeCount {

    public static Counter codeCount(Path path) throws IOException {
        Counter counter = new Counter();
        JavaFileVisitor visitor = new JavaFileVisitor(counter);
        Files.walkFileTree(path, visitor);
        return counter;
    }

    private static class JavaFileVisitor implements FileVisitor<Path> {

        private Counter counter ;
        public JavaFileVisitor(Counter counter){
            this.counter = counter;
        }

        private void codeCount1(Path path, Counter counter) throws IOException {
            final boolean[] inCommnet = {false};
            Files.lines(path).forEach((line) -> {
                if (line.matches("^[\\s&&[^\\n]]*$")) {
                    counter.blanks += 1;
                } else if (line.startsWith("/*") && !line.equals("*/")) {
                    counter.comments += 1;
                    inCommnet[0] = true;
                } else if (inCommnet[0] == true) {
                    counter.comments += 1;
                    if (line.endsWith("*/")) {
                        inCommnet[0] = false;
                    }
                } else if (line.startsWith("/*") && line.endsWith("*/")) {
                    counter.comments += 1;
                } else if (line.startsWith("//")) {
                    counter.comments += 1;
                } else {
                    counter.codes += 1;
                }
            });
        }

        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            if (file.toString().endsWith(".java")){
              //  System.out.println(file);
                codeCount1(file,counter);
            }
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
            System.out.println(file);
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
            return FileVisitResult.CONTINUE;
        }
    }

    public static class Counter {
        public int comments = 0;            //comment lines
        public int blanks = 0;              //empty lines
        public int codes = 0;               //code lines

        public void add(Counter counter) {
            this.comments += counter.comments;
            this.blanks += counter.blanks;
            this.codes += counter.codes;
        }

        @Override
        public String toString() {
            StringBuffer sb = new StringBuffer();
            String sp = System.getProperty("line.separator");
            sb.append("注释：").append(comments).append(sp);
            sb.append("空行：").append(blanks).append(sp);
            sb.append("代码：").append(codes).append(sp);
            return sb.toString();
        }
    }



    public static void main(String[] args) throws IOException {
        Path path = Paths.get("E:\\Documents\\GitHub\\hadoop-1.0.0\\src\\mapred");
        System.out.println(CodeCount.codeCount(path));
    }
}
