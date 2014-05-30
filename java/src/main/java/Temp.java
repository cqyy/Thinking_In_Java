/**
 * Created by Kali on 14-5-13.
 */
public class Temp {
    public static void main(String[] args) throws ClassNotFoundException {
        Class clazz = Class.forName("Temp",false,Temp.class.getClassLoader().getParent());
    }
}

