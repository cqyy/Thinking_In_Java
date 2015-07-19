package yuanye.reflection.proxy.stub;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by Kali on 14-5-29.
 * Default history do nothing.
 */
public class DefaultHistory implements History {
    @Override
    public long recordMethodCall(Proxy proxy, Method method, Object[] args) {
        return 0;
    }

    @Override
    public void recordMethodReturn(long callId, Object value) {

    }

    @Override
    public void recordMethodException(long callId, Throwable throwable) {

    }
}
