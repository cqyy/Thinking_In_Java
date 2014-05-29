package yuanye.reflection.proxy.proxychain;

import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by Kali on 14-5-29.
 */
public class SynchronizedProxy extends InvocationHandlerBase {

    public static Object createProxy(Object obj){
        return Proxy.newProxyInstance(
                obj.getClass().getClassLoader(),
                obj.getClass().getInterfaces(),
                new SynchronizedProxy(obj));
    }

    private SynchronizedProxy(Object target) {
        super(target);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        synchronized (this.realTarget()){
            System.out.println("synchronized " + this.realTarget);
            return method.invoke(nextTarget,args);
        }
    }


    public static void main(String... args){
        DoSomeThing doSomeThing = (DoSomeThing)SynchronizedProxy.createProxy(
                PrintWriterIH.getProxy(
                        new PrintWriter(System.out),
                        new DoSomeThingIml()));
        doSomeThing.doSomeThing();
    }
}

interface DoSomeThing{
    void doSomeThing();
}

class DoSomeThingIml implements DoSomeThing{
    @Override
    public void doSomeThing() {
        System.out.println(this.getClass().getName() + " playing....");
    }
}