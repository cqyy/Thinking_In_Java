package yuanye.reflection.proxy.stub;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by Kali on 14-5-29.
 */
public class StubIH implements Stub,InvocationHandler {

    private History history = new DefaultHistory();
    private ReturnValueStrategy strategy = new DefaultReturnValueStradegy();


    private StubIH(History history,ReturnValueStrategy strategy){
        if (history != null){this.history = history;}
        if (strategy != null){this.strategy = strategy;}
    }

    public static Stub createProxy(Class<?>[] interfaces,ReturnValueStrategy strategy){
        return createProxy(interfaces,strategy, null);
    }

    public static Stub createProxy(Class<?>[] interfaces,ReturnValueStrategy strategy,History history){

        for(Class<?> ifa : interfaces){
            if (ifa == Stub.class || ifa == InvocationHandler.class){
                throw new RuntimeException("Cannot stub " + ifa);
            }
        }
        Class<?>[] ifas = new Class[interfaces.length +1];
        ifas[0] = Stub.class;
        System.arraycopy(interfaces,0,ifas,1,interfaces.length);

        return (Stub)Proxy.newProxyInstance(
                Stub.class.getClassLoader(),
                interfaces,
                new StubIH(history,strategy));
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getDeclaringClass() == Stub.class){
            return method.invoke(this,args);
        }
        long callId = history.recordMethodCall((Proxy) proxy,method,args);
        try{
        Object result = strategy.getReturnValue((Proxy) proxy,method,args,history);
        history.recordMethodReturn(callId, result);
            return result;
        }catch (WrappedException e){
            history.recordMethodException((Long) proxy,e.getCause());
            throw e.getCause();
        }catch (Exception e){
            history.recordMethodException(callId,e);
            throw e;
        }
    }

    @Override
    public History getHistory() {
        return history;
    }
}
