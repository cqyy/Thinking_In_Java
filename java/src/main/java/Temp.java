import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Kali on 14-5-13.
 */
public class Temp {

    private static class MyInvocationHandler implements InvocationHandler{

        MySayHello hello = new MySayHello();

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            System.out.println(method.getName() + " invoke ");
            Object result = method.invoke(hello,args);
            System.out.println(method.getName() + " return");
            return result;
        }
    }

    private static interface SayHello{
        void sayHello();
    }

    private static class MySayHello implements SayHello{
        @Override
        public void sayHello() {
            System.out.println(" Hello --- " + this.getClass().getName());
        }
    }

    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Class<?> clazz = Class.forName(Collection.class.getName(),false, ArrayList.class.getClassLoader());
        clazz.newInstance();
    }
}

