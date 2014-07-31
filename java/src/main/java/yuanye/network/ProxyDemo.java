package yuanye.network;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * Created by Kali on 14-8-31.
 */
public class ProxyDemo {

    public static void main(String[] args) {
        SocketAddress proxyAddr = new InetSocketAddress("127.0.0.1",8087);
        Proxy goagent = new Proxy(Proxy.Type.HTTP,proxyAddr);
        Socket socket = new Socket(goagent);
        SocketAddress youtuebe = new InetSocketAddress("www.youtube.com",80);
        try{
            socket.connect(youtuebe);
            System.out.println("youtube connected");
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try {
                socket.close();
            } catch (IOException e) {
                //ignore
            }
        }
    }
}
