package yuanye.network;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.InetAddress;
import java.util.Arrays;

/**
 * Created by Kali on 14-8-31.
 */
public class SecureSocketDemo {

    public static void main(String[] args) {
        final String host = "www.baidu.com";
        SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        try(SSLSocket socket = (SSLSocket) factory.createSocket(
                InetAddress.getByName("www.baidu.com"),443)){
            String[] suits = socket.getSupportedCipherSuites();
            socket.setEnabledCipherSuites(suits);
            Writer out = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");
            // https requires the full URL in the GET line
            out.write("GET http://" + host + "/ HTTP/1.1\r\n");
            out.write("Host: " + host + "\r\n");
            out.write("\r\n");
            out.flush();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(
                            socket.getInputStream()));
            String line;
            while ( !(line = reader.readLine()).equals("")){
                System.out.println(line);
            }
            System.out.println();

            // read the length
            String contentLength = reader.readLine();
            int length = Integer.MAX_VALUE;
            try {
                length = Integer.parseInt(contentLength.trim(), 16);
            } catch (NumberFormatException ex) {
               // This server doesn't send the content-length
               // in the first line of the response body
            }
            System.out.println(contentLength);
            int c;
            int i = 0;
            while ((c = reader.read()) != -1 && i++ < length) {
                System.out.write(c);
            }
        }catch (IOException ie){
            ie.printStackTrace();
        }
    }
}
