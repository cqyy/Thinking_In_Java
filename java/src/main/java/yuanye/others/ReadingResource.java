package yuanye.others;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

/**
 * Created by Administrator on 2014/8/2.
 */
public class ReadingResource {
    private static final String FILE_NAME = "/app.properties";

    private static void  method1() throws IOException {
        Properties conf = new Properties();
        InputStream inputStream = ReadingResource.class.getResourceAsStream(FILE_NAME);
        conf.load(inputStream);
        inputStream.close();
        System.out.println(conf.get("age"));
    }

    private static void method2() throws IOException {
        Properties conf = new Properties();
        URL resourceURL = ReadingResource.class.getResource(FILE_NAME);
        conf.load(resourceURL.openStream());
        System.out.println(conf.get("name"));
    }

    public static void main(String[] args) throws IOException {
       method1();
       method2();
    }
}
