package yuanye.reflection.classloader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by Kali on 14-5-30.
 */
public class SimpleClassLoader extends ClassLoader {


    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        try {
            System.out.println("SimpleClassLoader findClass");
            File file = new File("D:/Documents/GitHub/Thinking_In_Java/java/target/classes/yuanye/reflection/classloader/LoadClass.class");
            byte[] bytes = new byte[(int) file.length()];
            FileInputStream inputStream = new FileInputStream(file);
            inputStream.read(bytes);
            Class clazz = defineClass("yuanye.reflection.classloader.LoadClass",bytes,0,bytes.length);
            return clazz;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new ClassNotFoundException("yuanye.reflection.classloader.LoadClass");
    }


    public static void main(String... args) throws ClassNotFoundException {
        ClassLoader loader = new SimpleClassLoader();
        Class clazz = loader.loadClass("noexist");
        System.out.println(clazz);

    }
}
