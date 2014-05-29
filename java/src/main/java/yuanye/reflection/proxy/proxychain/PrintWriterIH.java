package yuanye.reflection.proxy.proxychain;

import java.io.PrintWriter;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by Kali on 14-5-29.
 */
public class PrintWriterIH implements InvocationHandler {

    private PrintWriter writer;
    private Object target;

    private PrintWriterIH(PrintWriter writer,Object target){
        this.writer = writer;
        this.target = target;
    }

    public static Object getProxy(PrintWriter writer,Object target){
        return Proxy.newProxyInstance(
                target.getClass().getClassLoader(),
                target.getClass().getInterfaces(),
                new PrintWriterIH(writer, target));
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        writer.println(method.getName() + " invocate");
        Object ret = method.invoke(target,args);
        writer.println(method.getName() + " return ");
        return ret;
    }
}
