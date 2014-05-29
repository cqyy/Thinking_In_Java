package yuanye.reflection.proxy.stub;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by Kali on 14-5-29.
 */
public interface History {
    long recordMethodCall(Proxy proxy,Method method,Object[] args);
    void recordMethodReturn(long callId,Object value);
    void recordMethodException(long callId,Throwable throwable);

}
