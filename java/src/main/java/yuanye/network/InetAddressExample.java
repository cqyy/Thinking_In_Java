package yuanye.network;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by Kali on 14-8-26.
 * Get IP address of given host name.
 */
public class InetAddressExample {

    public static void main(String[] args) {

        try{
            InetAddress local = InetAddress.getLocalHost();
            System.out.println("Local Host");
            System.out.println("\t" + local.getHostName());
            System.out.println("\t" + local.getHostAddress());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

       for(String name : args){
           System.out.println(name);
           try {
               InetAddress[] addresses = InetAddress.getAllByName(name);
               for(InetAddress address : addresses){
                   System.out.println("\t" + address.getHostAddress());
               }
           } catch (UnknownHostException e) {
               System.out.println("Can't resolve host :" + name);
           }
       }
    }
}
